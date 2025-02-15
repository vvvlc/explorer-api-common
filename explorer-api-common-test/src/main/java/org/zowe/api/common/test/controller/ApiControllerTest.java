/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright IBM Corporation 2018, 2019
 */
package org.zowe.api.common.test.controller;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.zowe.api.common.exceptions.ZoweRestExceptionHandler;
import org.zowe.api.common.test.ZoweApiTest;
import org.zowe.api.common.utils.ZosUtils;

import static org.mockito.Mockito.when;

public abstract class ApiControllerTest extends ZoweApiTest {

    protected static final String DUMMY_USER = "A_USER";
    protected static final String EMPTY_ITEMS = "{\"items\":[]}";

    protected MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(getController()).setControllerAdvice(new ZoweRestExceptionHandler())
            .build();
        PowerMockito.mockStatic(ZosUtils.class);
        when(ZosUtils.getUsername()).thenReturn(DUMMY_USER);
    }

    public abstract Object getController();

}
