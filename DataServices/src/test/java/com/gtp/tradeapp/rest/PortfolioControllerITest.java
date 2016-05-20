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

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class PortfolioControllerITest extends RestIT {

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
    public void shouldReturnPortfolioAmount() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedContent = IOUtils.toString(classLoader.getResourceAsStream("controller/portfolio/expectedPortfolioAmount.json"));

        mockMvc.perform(get("/portfolio/amount")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("username", "UnitTester@dummy.com")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expectedContent));

    }

    @Test
    public void shouldReturnPortfolioDetails() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        mockMvc.perform(get("/portfolio/details")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.summary.cash", is(1000000.00)))
                .andExpect(jsonPath("$.summary.totalPortfolio", greaterThan(0.00)))
                .andExpect(jsonPath("$.summary.netAssetValue", greaterThan(1000000.00)))
                .andExpect(jsonPath("$.summary.delta", greaterThan(0.00)))
                .andExpect(jsonPath("$.portfolio.", hasSize(2)))
                .andExpect(jsonPath("$.user.username", is("shari@dummy.com")));
    }

    @Test
    public void shouldReturnPortfolioDetailsZero() throws Exception {
        final String accessToken = getAccessToken("junaid@dummy.com", "spring");

        mockMvc.perform(get("/portfolio/details")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.portfolio.", isEmptyOrNullString()));
    }

    @Test
    public void shouldReturnPortfolioHistoryDetails() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedContent = IOUtils.toString(classLoader.getResourceAsStream("controller/portfolio/expectedPortfolioHistory.json"));

        mockMvc.perform(get("/portfolio/history/details")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expectedContent));

        // Reference: controller/portfolio/expectedPortfolioDetails.json
    }

    //TODO:: Add Test for Empty List
}