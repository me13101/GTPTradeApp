package com.gtp.tradeapp.service.portfolio;

import com.gtp.tradeapp.builder.PositionBuilder;
import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.domain.AssetType;
import com.gtp.tradeapp.domain.CurrentPosition;
import com.gtp.tradeapp.domain.Indicator;
import com.gtp.tradeapp.domain.marketdata.MarketInstrument;
import com.gtp.tradeapp.entity.HistoricalPortfolio;
import com.gtp.tradeapp.entity.Position;
import com.gtp.tradeapp.entity.Transaction;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.repository.AssetRepository;
import com.gtp.tradeapp.repository.HistoricalPortfolioRepository;
import com.gtp.tradeapp.repository.PositionRepository;
import com.gtp.tradeapp.repository.UserRepository;
import com.gtp.tradeapp.service.marketdata.MarketDataService;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.gtp.tradeapp.domain.AssetClass.Commodities;
import static com.gtp.tradeapp.domain.AssetClass.Equity;
import static com.gtp.tradeapp.domain.AssetClass.FixedIncome;
import static com.gtp.tradeapp.domain.AssetType.index;
import static com.gtp.tradeapp.domain.AssetType.stock;
import static java.math.BigDecimal.ROUND_HALF_DOWN;
import static java.math.BigDecimal.ZERO;
import static java.math.RoundingMode.HALF_DOWN;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    HistoricalPortfolioRepository historicalPortfolioRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    MarketDataService marketDataService;

    @Autowired
    AssetRepository assetRepository;

    @Override
    public BigDecimal getCurrentCashAmount(User user) {
        User persistedUser = userRepository.findByUsername(user.getUserUsername()).get();
        return persistedUser.getCash();
    }

    @Override
    public BigDecimal getTotalPortfolioAmount(User user) {
        return getTotalPortfolio(user);
    }

    @Override
    public List<HistoricalPortfolio> getHistoricalPortfolio(User user) {
        return getHistoricalPortfolioDesc(user);
    }

    @Override
    public Map<String, BigDecimal> getAssetSummary(User user) {
        return new HashMap<String, BigDecimal>() {
            {
                put("cash", getCurrentCashAmount(user));
                put("totalPortfolio", getTotalPortfolio(user));
                put("netAssetValue", getCurrentNav(user));
                put("delta", getUserDelta(user));
                put("beta", getUserBeta(user));
                put("ytd", getCurrentNav(user).subtract(BigDecimal.valueOf(1000000)));
            }
        };
    }

    @Override
    public List<CurrentPosition> getCurrentPosition(User user) {
        return getUserCurrentPosition(user);
    }

    @Override
    public Map<AssetType, Double> getAssetTypeDistribution(List<CurrentPosition> currentPositions) {
        BigDecimal totalStock = getTotal(currentPositions, stock);
        BigDecimal totalIndex = getTotal(currentPositions, index);

        return new HashMap<AssetType, Double>() {
            {
                BigDecimal totalPortfolio = totalIndex.add(totalStock);
                if (ZERO.compareTo(totalPortfolio) == 0) {
                    put(stock, 0.0);
                    put(index, 0.0);
                } else {
                    put(stock, (totalStock.divide(totalPortfolio, ROUND_HALF_DOWN).doubleValue()));
                    put(index, (totalIndex.divide(totalPortfolio, ROUND_HALF_DOWN).doubleValue()));
                }
            }
        };
    }

    @Override
    public Map<AssetClass, Double> getAssetClassDistribution(List<CurrentPosition> currentPositions) {
        BigDecimal totalEquity = getTotal(currentPositions, Equity);
        BigDecimal totalCommodities = getTotal(currentPositions, Commodities);
        BigDecimal totalFixedIncome = getTotal(currentPositions, FixedIncome);

        return new HashMap<AssetClass, Double>() {
            {
                BigDecimal totalPortfolio = totalEquity.add(totalCommodities).add(totalFixedIncome);
                if (ZERO.compareTo(totalPortfolio) == 0) {
                    put(Equity, 0.0);
                    put(Commodities, 0.0);
                    put(FixedIncome, 0.0);
                } else {
                    put(Equity, (totalEquity.divide(totalPortfolio, ROUND_HALF_DOWN).doubleValue()));
                    put(Commodities, (totalCommodities.divide(totalPortfolio, ROUND_HALF_DOWN).doubleValue()));
                    put(FixedIncome, (totalFixedIncome.divide(totalPortfolio, ROUND_HALF_DOWN).doubleValue()));
                }
            }
        };
    }

    @Override
    public boolean checkForPosition(Transaction updatedTransaction) {
        Optional<Position> position =
                positionRepository.findStockByUserIdAndStockId(updatedTransaction.getUserId(), updatedTransaction.getTicker());

        if (position.isPresent()) {
            if (position.get().getQuantity() >= updatedTransaction.getQty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void updatePosition(final Transaction newTransaction) {
        int quantity = newTransaction.getQty();

        Optional<Position> stockByUserIdAndStockId =
                positionRepository.findStockByUserIdAndStockId(newTransaction.getUserId(), newTransaction.getTicker());

        if (stockByUserIdAndStockId.isPresent()) {
            quantity += stockByUserIdAndStockId.get().getQuantity();
        }
        positionRepository.save(
                new PositionBuilder()
                        .withQuantity(quantity)
                        .withStockId(newTransaction.getTicker())
                        .withUserId(newTransaction.getUserId())
                        .build()
        );
    }

    private List<CurrentPosition> getUserCurrentPosition(User user) {
        Optional<List<Position>> positionList = positionRepository
                .findStockByUserId(user.getId());
        if (positionList.isPresent()) {
            List<CurrentPosition> positions = positionList.get().stream()
                    .filter(s -> s.getQuantity() > 0)
                    .map(CurrentPosition::new)
                    .collect(Collectors.toList());

            positions.forEach(p -> {
                p.setCurrentPrice(getPriceFor(p.getStockId()));
                p.setBeta(getBetaFor(p.getStockId()));
                p.setAsset(assetRepository.findOne(p.getStockId()));
                p.setTotalAmount(p.getCurrentPrice().multiply(BigDecimal.valueOf(p.getQuantity())));
            });

            return positions;
        }
        return Collections.emptyList();
    }

    private BigDecimal getTotal(List<CurrentPosition> currentPositions, AssetType assetType) {
        try {
            return currentPositions.stream()
                    .filter(s -> s.getAsset().getType() == assetType)
                    .map(CurrentPosition::getTotalAmount)
                    .reduce(ZERO, BigDecimal::add);
        } catch (Exception e) {
            return ZERO;
        }
    }

    private BigDecimal getTotal(List<CurrentPosition> currentPositions, AssetClass assetClass) {
        try {
            return currentPositions.stream()
                    .filter(s -> s.getAsset().getAssetClass() == assetClass)
                    .map(CurrentPosition::getTotalAmount)
                    .reduce(ZERO, BigDecimal::add);
        } catch (Exception e) {
            return ZERO;
        }
    }

    public BigDecimal getTotalPortfolio(User user) {
        //GetTotalPortfolio
        List<Position> positions = positionRepository.findStockByUserId(user.getId()).get();

        return positions.stream()
                .map(calcTotalForStock())
                .reduce(ZERO, BigDecimal::add);
    }

    private BigDecimal getUserBeta(User user) {
        List<CurrentPosition> currentPosition = getUserCurrentPosition(user);
        if (!currentPosition.isEmpty()) {
            currentPosition.forEach(
                    currentPosition1 -> {
                        currentPosition1.setBeta(toSetBeta(currentPosition1.getStockId()));
                    }
            );
            return calculateBeta(currentPosition);
        }
        return BigDecimal.ONE;
    }

    private BigDecimal getUserDelta(User user) {
        //TODO: Validate for Yesterday
        List<HistoricalPortfolio> historicalPortfolioList = getHistoricalPortfolioDesc(user);

        if (historicalPortfolioList.size() > 0) {
            return calcNavDelta(
                    getLastNav(historicalPortfolioList),
                    getCurrentNav(user));
        }
        return ZERO;
    }

    Double toSetBeta(String stock){
        String input = String.valueOf(marketDataService.getIndicatorStockPrice(giveParams(stock)));
        JSONObject parent = new JSONObject(input);
        JSONArray valuelist = parent.getJSONArray("valuelist");
        JSONObject beta = (JSONObject)valuelist.get(0);
        return (double)beta.get("beta");
    }

    Map<String, Object> giveParams (String stock){
        return  new HashMap<String, Object>() {{
            put("indicator", Indicator.beta);
            put("stock", stock);
            put("days", "12");
            put("interval", "1");
        }};
    }

    private BigDecimal calculateBeta(List<CurrentPosition> currentPosition) {
        Double dividend = currentPosition.stream()
                .map(current -> current.getBeta() * current.getQuantity())
                .reduce(Double::sum)
                .orElse((double) 0);

        Integer divisor = currentPosition.stream()
                .map(Position::getQuantity)
                .reduce(Integer::sum)
                .orElse(0);

        if (dividend == 0) {
            return ZERO;
        }
        return BigDecimal.valueOf(dividend / divisor);
    }

    private Function<Position, BigDecimal> calcTotalForStock() {
        return s -> getPriceFor(s.getStockId())
                .multiply(BigDecimal.valueOf(s.getQuantity()));
    }

    private BigDecimal getCurrentNav(User user) {
        return getCurrentCashAmount(user).add(getTotalPortfolio(user));
    }

    private BigDecimal getLastNav(List<HistoricalPortfolio> historicalPortfolioList) {
        return historicalPortfolioList.get(0).getNetAssetValue();
    }

    private BigDecimal calcNavDelta(BigDecimal lastNav, BigDecimal currentNav) {
        return (lastNav.subtract(currentNav)).divide(lastNav, HALF_DOWN).negate();
    }

    private BigDecimal getPriceFor(String stockId) {
        MarketInstrument latestStockPrice = marketDataService.getLatestStockPrice(stockId);
        return latestStockPrice.getStocklist().get(0).getPrice();
    }

    private Double getBetaFor(String stockId) {
        MarketInstrument latestStockPrice = marketDataService.getLatestStockPrice(stockId);
        //TODO:: TBD
        return latestStockPrice.getStocklist().get(0).getData().getPricechange().doubleValue();
    }

    private List<HistoricalPortfolio> getHistoricalPortfolioDesc(User user) {
        return historicalPortfolioRepository.findByUserIdOrderByPortfolioDateDesc(user.getId()).get();
    }

}
