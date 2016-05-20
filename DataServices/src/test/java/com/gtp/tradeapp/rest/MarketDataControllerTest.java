package com.gtp.tradeapp.rest;

import com.gtp.tradeapp.Application;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class MarketDataControllerTest extends RestIT {

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
    public void shouldReturnAllLatestStockPrice() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        mockMvc.perform(get("/marketdata/get/all/latest")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.stocklist.", Matchers.hasSize(41)));
    }

    @Test
    public void shouldReturnAllTypeLatestStockPrice() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        mockMvc.perform(get("/marketdata/get/all/latest")
                        .param("assetType", "stock")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.stocklist.", Matchers.hasSize(36)));

        mockMvc.perform(get("/marketdata/get/all/latest")
                        .param("assetType", "index")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.stocklist.", Matchers.hasSize(5)));
    }

    @Test
    public void shouldReturnGetLatestMarketPrice() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        mockMvc.perform(get("/marketdata/get/latest")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("stock", "AAPL")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.stocklist", hasSize(1)));
    }

    @Test
    public void shouldReturnHistoricalStockPrice() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        mockMvc.perform(get("/marketdata/get/historical")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("stock", "AAPL")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.pricelist.", hasSize(greaterThan(200))));
    }

    @Test
    public void shouldReturnStockList() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedContent = IOUtils.toString(classLoader.getResourceAsStream("controller/asset/expectedStockList.json"));

        mockMvc.perform(get("/marketdata/get/list")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expectedContent));

    }

    @Test
    public void shouldReturnIndicators() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        mockMvc.perform(get("/marketdata/get/indicator/{indicator}/", "SMA")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("stock", "AAPL")
                        .param("days", "26")
                        .param("interval", "1")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                For Reference : controller/marketdata/expectedSMA.json
//                .andExpect(content().string())
                .andExpect(jsonPath("$.valuelist", hasSize(greaterThan(200))));
    }

    @Test
    public void shouldReturnStockMetrics() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        mockMvc.perform(get("/marketdata/get/metrics")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("stock", "AAPL")
                        .param("days", "26")
                        .param("interval", "1")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
//                For Reference : controller/marketdata/expectedSMA.json
//                .andExpect(content().string())
    }
}
