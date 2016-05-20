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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class TransactionControllerTest extends RestIT {

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
    public void shouldReturnTransactionBuyUnsuccessfulWhenUserOverBought() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedContent = IOUtils.toString(classLoader.getResourceAsStream("controller/transaction/expectedTransactionBuyFail.json"));

        mockMvc.perform(post("/trading/buy")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("username", "shari@dummy.com")
                        .content("{\"ticker\": \"UBS\", \"qty\":1000000}")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expectedContent));

        validateProfileDetails(accessToken, 2);
    }

    @Test
    public void shouldReturnTransactionBuyUnsuccessfulWhenQuantityIsNegative() throws Exception {
        mockMvc.perform(post("/trading/buy")
                        .header("Authorization", "Bearer " + getAccessToken("shari@dummy.com", "spring"))
                        .param("username", "shari@dummy.com")
                        .content("{\"ticker\": \"UBS\", \"qty\":-1000000}")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().is(412));
    }

    @Test
    public void shouldReturnTransactionBuySuccessfulWhenCashIsSufficient() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedContent = IOUtils.toString(classLoader.getResourceAsStream("controller/transaction/expectedTransactionBuy.json"));

        mockMvc.perform(post("/trading/buy")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("username", "shari@dummy.com")
                        .content("{\"ticker\": \"UBS\", \"qty\":1000}")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expectedContent));

        validateProfileDetails(accessToken, 2);
    }

    @Test
    public void shouldReturnTransactionBuyNewSuccessfulWhenCashIsSufficient() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedContent = IOUtils.toString(classLoader.getResourceAsStream("controller/transaction/expectedTransactionBuy.json"));

        mockMvc.perform(post("/trading/buy")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("username", "shari@dummy.com")
                        .content("{\"ticker\": \"TW\", \"qty\":1000}")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expectedContent));

        validateProfileDetails(accessToken, 3);
    }

    @Test
    public void shouldReturnTransactionSellUnsuccessfulWhenUserOverBought() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedContent = IOUtils.toString(classLoader.getResourceAsStream("controller/transaction/expectedTransactionSellFail.json"));

        mockMvc.perform(post("/trading/sell")
                        .header("Authorization", "Bearer " + accessToken)
                        .content("{\"ticker\": \"UBS\", \"qty\":1000000}")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expectedContent));

        validateProfileDetails(accessToken, 2);
    }

    @Test
    public void shouldReturnTransactionSellUnsuccessfulWhenQuantityIsNegative() throws Exception {
        mockMvc.perform(post("/trading/sell")
                        .header("Authorization", "Bearer " + getAccessToken("shari@dummy.com", "spring"))
                        .param("username", "shari@dummy.com")
                        .content("{\"ticker\": \"UBS\", \"qty\":-10}")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().is(412));
    }

    @Test
    public void shouldReturnTransactionSellSuccessfulWhenCashIsSufficient() throws Exception {
        final String accessToken = getAccessToken("shari@dummy.com", "spring");

        ClassLoader classLoader = getClass().getClassLoader();
        String expectedContent = IOUtils.toString(classLoader.getResourceAsStream("controller/transaction/expectedTransactionSell.json"));

        mockMvc.perform(post("/trading/sell")
                        .header("Authorization", "Bearer " + accessToken)
                        .content("{\"ticker\": \"UBS\", \"qty\":10}")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(expectedContent));

        validateProfileDetails(accessToken, 2);
    }

    @Test
    public void shouldReturnSpecifiedTransactionHistory() throws Exception {
        final String accessToken = getAccessToken("luck.lim.gtp2015@outlook.com", "123456");

        mockMvc.perform(get("/trading/history")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("limit", "10")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.", hasSize(10)));
    }

    @Test
    public void shouldReturnDefaultTransactionHistory() throws Exception {
        final String accessToken = getAccessToken("luck.lim.gtp2015@outlook.com", "123456");

        mockMvc.perform(get("/trading/history")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.", hasSize(50)));
    }

    @Test
    public void shouldReturnErrorTransactionHistory() throws Exception {
        final String accessToken = getAccessToken("luck.lim.gtp2015@outlook.com", "123456");

        mockMvc.perform(get("/trading/history")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("limit", "-10")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    public void shouldReturnMaxTransactionHistory() throws Exception {
        final String accessToken = getAccessToken("luck.lim.gtp2015@outlook.com", "123456");

        mockMvc.perform(get("/trading/history")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("limit", "100")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.", hasSize(56)));
    }

    @Test
    public void shouldReturnNoTransactionHistory() throws Exception {
        final String accessToken = getAccessToken("junaid@dummy.com", "spring");

        mockMvc.perform(get("/trading/history")
                        .header("Authorization", "Bearer " + accessToken)
                        .param("limit", "100")
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.", hasSize(0)));
    }

    private ResultActions validateProfileDetails(String accessToken, int size) throws Exception {
        return mockMvc.perform(get("/portfolio/details")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json;charset=UTF-8")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                For Debugging
//                Reference: controller/portfolio/expectedPortfolioDetails.json
                .andExpect(jsonPath("$.summary.cash", greaterThan(0.00)))
                .andExpect(jsonPath("$.summary.totalPortfolio", greaterThan(0.00)))
                .andExpect(jsonPath("$.summary.netAssetValue", greaterThan(1000000.00)))
                .andExpect(jsonPath("$.summary.delta", greaterThan(0.00)))
                .andExpect(jsonPath("$.portfolio.", hasSize(size)))
                .andExpect(jsonPath("$.user.username", is("shari@dummy.com")));
    }
}