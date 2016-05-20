package com.gtp.tradeapp.service.portfolio;

import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.domain.AssetType;
import com.gtp.tradeapp.domain.CurrentPosition;
import com.gtp.tradeapp.entity.HistoricalPortfolio;
import com.gtp.tradeapp.entity.Transaction;
import com.gtp.tradeapp.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PortfolioService {

    BigDecimal getCurrentCashAmount(User user);

    BigDecimal getTotalPortfolioAmount(User user);

    List<HistoricalPortfolio> getHistoricalPortfolio(User user);

    Map<String, BigDecimal> getAssetSummary(User user);

    List<CurrentPosition> getCurrentPosition(User user);

    Map<AssetType, Double> getAssetTypeDistribution(List<CurrentPosition> currentPositions);

    Map<AssetClass, Double> getAssetClassDistribution(List<CurrentPosition> currentPositions);

    boolean checkForPosition(Transaction updatedTransaction);

    void updatePosition(Transaction newTransaction);
}
