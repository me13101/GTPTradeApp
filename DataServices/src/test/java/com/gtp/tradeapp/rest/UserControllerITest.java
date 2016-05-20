/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gtp.tradeapp.rest;

import com.gtp.tradeapp.Application;
import com.gtp.tradeapp.service.email.EmailService;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class UserControllerITest extends RestIT {

    @Autowired
    WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void usersListEndpointAuthorized() throws Exception {
        mockMvc.perform(get("/user/list")
                .header("Authorization", "Bearer " + getAccessToken("roy@dummy.com", "spring")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(9)));
    }

    @Test
    public void usersListEndpointAccessDenied() throws Exception {
        mockMvc.perform(get("/user/list")
                .header("Authorization", "Bearer " + getAccessToken("kingsley@dummy.com", "spring")))
                .andExpect(status().is(403));
    }

    @Test
    public void usersCreateEndpointShouldCreateSuccessfully() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        String expectedContent = IOUtils.toString(classLoader.getResourceAsStream("controller/user/sampleUser.json"));

        mockMvc.perform(post("/user/create")
                        .contentType("application/json;charset=UTF-8")
                        .content(expectedContent)
        )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":1,\"message\":\"User added Successfully!\"}"));
    }

    @Test
    public void usersCreateEndpointShouldFailCreate() throws Exception {
        mockMvc.perform(post("/user/create")
                        .contentType("application/json;charset=UTF-8")
                        .content("{\n" +
                                "  \"firstname\": \"Lucky\",\n" +
                                "  \"lastname\": \"lim\",\n" +
                                "  \"username\": \"limluc@outlook.com\"" +
                                "}")
        )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":0,\"message\":\"java.lang.NullPointerException\"}"));
    }

    @Test
    public void usersIdShouldReturnUserId() throws Exception {
        mockMvc.perform(get("/user/id/1")
                .header("Authorization", "Bearer " + getAccessToken("roy@dummy.com", "spring")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("roy@dummy.com")));
    }

    @Test
    public void usersForgotPasswordShouldBeSuccessful() throws Exception {
        mockMvc.perform(post("/user/password/reset")
                .param("email", "analytics@dummy.com"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":1,\"message\":\"Email Has Been Sent to analytics@***\"}"));
    }

    @Test
    public void usersForgotPasswordShouldFailIfInvalid() throws Exception {
        mockMvc.perform(post("/user/password/reset")
                .param("email", "roy@dummy"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\":0,\"message\":\"Unable to reset password for roy@dummy\"}"));
    }

}
