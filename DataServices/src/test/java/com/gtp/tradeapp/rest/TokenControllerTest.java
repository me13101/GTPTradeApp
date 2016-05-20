package com.gtp.tradeapp.rest;

import com.gtp.tradeapp.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class TokenControllerTest extends RestIT {
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
    public void shouldRevokeToken() throws Exception {
        String accessToken = getAccessToken("roy@dummy.com", "spring");
        mockMvc.perform(post("/token/revoke")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"code\":1,\"message\":\"Token Removed\",\"info\":null}"));

        mockMvc.perform(get("/porfolio/details")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().is(401));
    }

    @Test
    public void shouldFailRevokeToken() throws Exception {
        String accessToken = getAccessToken("roy@dummy.com", "spring");
        mockMvc.perform(post("/token/revoke")
                .header("Authorization", "Bearer " + accessToken.hashCode()))
                .andExpect(status().is(401));

    }
}