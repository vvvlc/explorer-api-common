/**
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2019
 */
package org.zowe.api.common.connectors.zosmf;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.zowe.api.common.clients.http.ZoweHttpClientWrapper;
import org.zowe.api.common.test.ZoweApiTest;
import org.zowe.api.common.utils.ResponseUtils;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@PowerMockIgnore({ "javax.net.ssl.*" })
@RunWith(PowerMockRunner.class)
@PrepareForTest({ ResponseUtils.class, RequestBuilder.class, ZoweHttpClientWrapper.class })
public class ZosmfConnectorTest extends ZoweApiTest {

    private static final String USERNAME = "USER";
    private static final String PASSWORD = "PASSWORD";

    private static final String HOST_NAME = "zowe.org";
    private static final Integer PORT = 8443;

    @Mock
    ZoweHttpClientWrapper clientWrapper;

    ZosmfProperties properties = ZosmfProperties.builder().httpsPort(PORT).ipAddress(HOST_NAME).build();
    ZosmfConnector zosmfConnector = new ZosmfConnector(properties);

    // TODO - merge some mocking with *JobsService?

    @Test
    public void test_getLtpaHeader_works_with_good_response() throws Exception {
        Header header = mock(Header.class);
        setUpResponse(HttpStatus.SC_OK, header);
        assertEquals(header, zosmfConnector.getLtpaHeader(USERNAME, PASSWORD));
    }

    @Test
    public void test_getLtpaHeader_with_bad_status_code_throw_exception() throws Exception {
        Header header = mock(Header.class);
        setUpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, header);

        Exception expected = new IOException("login failed");

        shouldThrow(expected, () -> zosmfConnector.getLtpaHeader(USERNAME, PASSWORD));
    }

    @Test
    public void test_getLtpaHeader_with_no_header_throw_exception() throws Exception {
        setUpResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, null);

        Exception expected = new IOException("no header");

        shouldThrow(expected, () -> zosmfConnector.getLtpaHeader(USERNAME, PASSWORD));
    }

    private Header setUpResponse(int status, Header header)
            throws NoSuchAlgorithmException, KeyManagementException, IOException, ClientProtocolException {
        HttpClient client = mock(HttpClient.class);
        mockStatic(ZoweHttpClientWrapper.class);
        when(ZoweHttpClientWrapper.createIgnoreSSLClientWithPassword(USERNAME, PASSWORD)).thenReturn(client);

        RequestBuilder builder = mock(RequestBuilder.class);
        mockStatic(RequestBuilder.class);
        when(RequestBuilder.get(Mockito.any(URI.class))).thenReturn(builder);

        RequestBuilder builderWithHeader = mock(RequestBuilder.class);
        when(builder.addHeader("X-CSRF-ZOSMF-HEADER", "")).thenReturn(builderWithHeader);

        HttpUriRequest request = mock(HttpUriRequest.class);
        when(builderWithHeader.build()).thenReturn(request);

        HttpResponse response = mock(HttpResponse.class);
        mockStatic(ResponseUtils.class);
        when(ResponseUtils.getStatus(response)).thenReturn(status);

        when(response.getFirstHeader("Set-Cookie")).thenReturn(header);

        when(client.execute(request)).thenReturn(response);
        return header;
    }

}
