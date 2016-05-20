package com.gtp.tradeapp.service.portfolio;

import com.gtp.tradeapp.Application;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class PortfolioServiceImplTest {
    @Autowired
    WebApplicationContext context;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    PortfolioServiceImpl portfolioService;

    @Test
    public void shouldReturnBeta() throws Exception {
        Double aapl = portfolioService.toSetBeta("AAPL");

        assertEquals(Double.valueOf(-0.141803874875754), aapl);
    }
}