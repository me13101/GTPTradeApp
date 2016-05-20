package com.gtp.tradeapp.service.marketdata;

import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.domain.AssetType;
import com.gtp.tradeapp.domain.marketdata.MarketInstrument;
import com.gtp.tradeapp.entity.Asset;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MarketDataServiceStub implements MarketDataService {
    private final static Logger LOGGER = Logger.getLogger(MarketDataServiceStub.class);

    @Override
    public MarketInstrument getLatestStockPrice(String stockId) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String s = IOUtils.toString(
                    getClass().getClassLoader().getResourceAsStream("market-data/latestOneStock.json"));

            return mapper.readValue(s, MarketInstrument.class);
        } catch (IOException e) {
            LOGGER.error("Unable to parse samplefile", e);
            return null;
        }
    }

    @Override
    public String getHistoricalStockPrice(String stockId) {
        try {
            return IOUtils.toString(
                    getClass().getClassLoader().getResourceAsStream("market-data/historicalOneStock.json"));
        } catch (IOException e) {
            LOGGER.error("Unable to parse Sample File", e);
            return null;
        }
    }

    @Override
    public MarketInstrument getLatestMarketData(AssetType assetType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String s = IOUtils.toString(
                    getClass().getClassLoader().getResourceAsStream("market-data/latestAllStocks.json"));

            return mapper.readValue(s, MarketInstrument.class);
        } catch (IOException e) {
            LOGGER.error("Unable to parse Sample File", e);
            return null;
        }
    }

    @Override
    public String getStockPriceMetric(Map<String, Object> map) {
        return null;
    }

    @Override
    public String getIndicatorStockPrice(Map<String, Object> params) {
        try {
            return IOUtils.toString(
                    getClass().getClassLoader().getResourceAsStream("market-data/latestAllStocks.json"));
        } catch (IOException e) {
            LOGGER.error("Unable to parse Sample File", e);
            return null;
        }
    }

    @Override
    public Iterable<Asset> getListOfStocks() {
        Asset myAsset = new Asset("ALGT", "Allegiant Travel Company", AssetClass.Equity, AssetType.stock, "Travel & Leisure", "EU");
        ArrayList<Asset> myAssetList = new ArrayList<Asset>();
        myAssetList.add(myAsset);
        return myAssetList;
    }
}
