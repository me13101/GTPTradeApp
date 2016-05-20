package com.gtp.tradeapp.domain.marketdata;

import java.util.List;

public class MarketInstrument {

    List<InstrumentAsset> stocklist;

    public MarketInstrument() {
    }

    public MarketInstrument(List<InstrumentAsset> stocklist) {
        this.stocklist = stocklist;
    }

    public List<InstrumentAsset> getStocklist() {
        return stocklist;
    }

    public void setStocklist(List<InstrumentAsset> stocklist) {
        this.stocklist = stocklist;
    }
}
