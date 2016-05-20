package com.gtp.tradeapp.rest;

import com.gtp.tradeapp.domain.AssetType;
import com.gtp.tradeapp.domain.Indicator;
import com.gtp.tradeapp.domain.marketdata.MarketInstrument;
import com.gtp.tradeapp.service.marketdata.MarketDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/marketdata")
public class MarketDataController {

    @Autowired
    MarketDataService dataService;

    @RequestMapping(value = "/get/all/latest", produces = "application/json;charset=UTF-8")
    public MarketInstrument getLatestMarketData(@RequestParam(value = "assetType", required = false) AssetType assetType) {
        return dataService.getLatestMarketData(assetType);
    }

    @RequestMapping(value = "/get/latest", produces = "application/json;charset=UTF-8")
    public MarketInstrument getLatestStockPrice(@RequestParam(value = "stock") String stock) {
        return dataService.getLatestStockPrice(stock);
    }

    @RequestMapping(value = "/get/historical", produces = "application/json;charset=UTF-8")
    public Object getHistoricalStockPrice(@RequestParam(value = "stock") String stock) {
        return dataService.getHistoricalStockPrice(stock);
    }

    @RequestMapping(value = "/get/metrics", produces = "application/json;charset=UTF-8")
    public Object getStockPriceMetric(@RequestParam(value = "stock") String stock,
                                      @RequestParam(value = "days", required = false) String days,
                                      @RequestParam(value = "interval", required = false) String interval) {
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("stock", stock);
            put("days", days);
            put("interval", interval);

        }};
        return dataService.getStockPriceMetric(params);
    }

    @RequestMapping(value = "/get/indicator/{indicator}/", produces = "application/json;charset=UTF-8")
    public Object getIndicatorStockPrice(@PathVariable(value = "indicator") Indicator indicator,
                                         @RequestParam(value = "stock") String stock,
                                         @RequestParam(value = "days", required = false) String days,
                                         @RequestParam(value = "interval", required = false) String interval) {

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("indicator", indicator);
            put("stock", stock);
            put("days", days);
            put("interval", interval);

        }};
        return dataService.getIndicatorStockPrice(params);
    }

    @RequestMapping(value = "/get/list", produces = "application/json;charset=UTF-8")
    public Object getListOfStocks() {
        return new HashMap<String, Object>() {{
            put("stocklist", dataService.getListOfStocks());
        }};
    }
}

