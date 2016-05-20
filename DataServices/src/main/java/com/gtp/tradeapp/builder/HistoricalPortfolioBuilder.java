package com.gtp.tradeapp.builder;


import com.gtp.tradeapp.entity.HistoricalPortfolio;

import java.math.BigDecimal;

public class HistoricalPortfolioBuilder {
    private HistoricalPortfolio historicalPortfolio;

    public HistoricalPortfolioBuilder() {
        historicalPortfolio = new HistoricalPortfolio();
    }

    public HistoricalPortfolioBuilder(Long id, Long userId) {
        historicalPortfolio = new HistoricalPortfolio();
        historicalPortfolio.setUserId(userId);
        historicalPortfolio.setPortfolioId(id);
    }

    public HistoricalPortfolioBuilder withUserId(Long userId) {
        this.historicalPortfolio.setUserId(userId);
        return this;
    }

    public HistoricalPortfolioBuilder withCash(BigDecimal cash) {
        this.historicalPortfolio.setCash(cash);
        return this;
    }

    public HistoricalPortfolioBuilder withPortfolio(BigDecimal portfolio) {
        this.historicalPortfolio.setPortfolio(portfolio);
        return this;
    }

    public HistoricalPortfolio build() {
        this.historicalPortfolio.setNetAssetValue(
                historicalPortfolio.getCash().add(historicalPortfolio.getPortfolio())
        );
        return historicalPortfolio;
    }
}
