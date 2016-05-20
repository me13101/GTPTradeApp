package com.gtp.tradeapp.service.portfolio;

import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.domain.AssetType;
import com.gtp.tradeapp.domain.CurrentPosition;
import com.gtp.tradeapp.entity.HistoricalPortfolio;
import com.gtp.tradeapp.entity.Transaction;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.repository.HistoricalPortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Service

public class PortfolioServiceStub implements PortfolioService {

    @Autowired
    HistoricalPortfolioRepository historicalPortfolioRepository;

    @Override
    public BigDecimal getCurrentCashAmount(User user) {
        return new BigDecimal(1000000);
    }

    @Override
    public BigDecimal getTotalPortfolioAmount(User user) {
        return null;
    }

    @Override
    public List<HistoricalPortfolio> getHistoricalPortfolio(User user) {
        HistoricalPortfolio historicalPortfolio = new HistoricalPortfolio();
        historicalPortfolio.setCash(new BigDecimal(96.4));
        historicalPortfolio.setPortfolio(new BigDecimal(100));
        historicalPortfolio.setUserId(2L);
        historicalPortfolio.setPortfolioId(1L);
        List<HistoricalPortfolio> historicalPortfolios = new ArrayList<>();
        historicalPortfolios.add(historicalPortfolio);
        return historicalPortfolios;


    }

    private BigDecimal getTotalPortfolio(User user) {
        return new BigDecimal(11000);
    }

    @Override
    public Map<String, BigDecimal> getAssetSummary(User user) {
        return new HashMap<>();
    }

    @Override
    public List<CurrentPosition> getCurrentPosition(User user) {
        return null;
    }

    @Override
    public Map<AssetType, Double> getAssetTypeDistribution(List<CurrentPosition> currentPositions) {
        return null;
    }

    @Override
    public Map<AssetClass, Double> getAssetClassDistribution(List<CurrentPosition> currentPositions) {
        return null;
    }

    @Override
    public boolean checkForPosition(Transaction updatedTransaction) {
        return false;
    }

    @Override
    public void updatePosition(Transaction newTransaction) {
        //TODO:: To implement
    }
}
