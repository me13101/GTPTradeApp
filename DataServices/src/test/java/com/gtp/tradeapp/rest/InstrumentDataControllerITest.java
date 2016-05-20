package com.gtp.tradeapp.rest;

import com.gtp.tradeapp.Application;
import org.apache.commons.io.IOUtils;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class InstrumentDataControllerITest extends RestIT {

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
    public void shouldReturnStockList() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedContent = IOUtils.toString(classLoader.getResourceAsStream("controller/asset/expectedStockList.json"));

        mockMvc.perform(get("/marketdata/get/list")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("username", "UnitTester@dummy.com")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expectedContent));
    }
}
