/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2019
 */
package org.zowe.api.common.security;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.zowe.api.common.connectors.zosmf.ZosmfConnector;
import org.zowe.api.common.test.ZoweApiTest;

import static org.mockito.Mockito.when;

public class CustomAuthenticationProviderTest extends ZoweApiTest {

    private static final String USERNAME = "USER";
    private static final String PASSWORD = "PASSWORD";

    @Mock
    Authentication authentication;

    @Mock
    ZosmfConnector zosmfConnector;

    CustomAuthenticationProvider provider;

    @Before
    public void setUp() throws Exception {
        when(authentication.getName()).thenReturn(USERNAME);
        when(authentication.getCredentials()).thenReturn(PASSWORD);

        provider = new CustomAuthenticationProvider();
        provider.zosmfconnector = zosmfConnector;
    }

    @Test
    public void shouldThrowErrorIfLtpaNull() throws Exception {

//        Set-Cookie: LtpaToken2=u4Ws1e6+KF93MuHR5gnSjpzRTbfsUZLy9DIzNhrm8RlIMKI/Y/3OjcMrZvmUBj5pOmvJrsToclHGIVae53y2PzuQ3Pb4Zg7fN6sE2ig2JnQbXTnYglfL74rNQ7lHlRFH/UC2yWThtXcy+7lt2hJlahYQKd+AXL2X71m50/OCGDmHZ6y6FJEArIUyX546dQBt4wSuFwJAJ6uz7GGLU9I+1qvJxXbZ+VZ3jhW3BqqJ9I4/GfQAuCNxiy44d5wx/N8+/cjtqKJqx+mk8/JtY1t0GHY3I2LGfcTMe3rPshnNVfXvr41sJfd7NKdYd4dQRJx4; Path=/; Secure; HttpOnly

//        Exception expectedException = null;
//
//        shouldThrow(expectedException, () -> provider.authenticate(authentication));
    }

    // TODO test good case

}
