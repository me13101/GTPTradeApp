package com.gtp.tradeapp.service.marketdata;

import com.gtp.tradeapp.domain.AssetType;
import com.gtp.tradeapp.domain.marketdata.MarketInstrument;
import com.gtp.tradeapp.entity.Asset;

import java.util.Map;

public interface MarketDataService {

    MarketInstrument getLatestStockPrice(String stockId);

    String getHistoricalStockPrice(String stockId);

    MarketInstrument getLatestMarketData(AssetType assetType);

    String getStockPriceMetric(Map<String, Object> map);

    Object getIndicatorStockPrice(Map<String, Object> map);

    Iterable<Asset> getListOfStocks();

}
