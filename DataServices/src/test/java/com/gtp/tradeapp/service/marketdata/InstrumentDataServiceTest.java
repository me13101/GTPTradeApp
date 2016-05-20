package com.gtp.tradeapp.service.marketdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtp.tradeapp.domain.marketdata.MarketInstrument;
import com.gtp.tradeapp.repository.AssetRepository;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class InstrumentDataServiceTest {
    @InjectMocks
    private AssetRepository assetRepository;
    private MarketDataService marketDataService;

    @Before
    public void setUp() {
        this.marketDataService = new MarketDataServiceImpl(assetRepository);
    }

    @Test
    public void shouldGetStockPrice() throws IOException {
        String s = IOUtils.toString(
                getClass().getClassLoader().getResourceAsStream("controller/asset/expectedCurrentStock.json"));
        MarketInstrument marketInstrument = new ObjectMapper().readValue(s, MarketInstrument.class);

        assertEquals(1, marketInstrument.getStocklist().size());
        assertEquals(BigDecimal.valueOf(114.73), marketInstrument.getStocklist().get(0).getPrice());
    }
}