package com.gtp.tradeapp.service.marketdata;

import com.gtp.tradeapp.Application;
import com.gtp.tradeapp.domain.marketdata.MarketInstrument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class MarketDataServiceImplTest {

    MarketDataService marketDataService;

    @Test
    public void shouldReturnStock() throws Exception {
        marketDataService = new MarketDataServiceImpl(null);
        MarketInstrument aapl = marketDataService.getLatestStockPrice("AAPL");

        assertEquals(1, aapl.getStocklist().size());
    }
}