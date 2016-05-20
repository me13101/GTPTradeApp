package com.gtp.tradeapp.service.portfolio;

import com.gtp.tradeapp.builder.HistoricalPortfolioBuilder;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.repository.HistoricalPortfolioRepository;
import com.gtp.tradeapp.service.marketdata.MarketDataService;
import com.gtp.tradeapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoricalPortfolioServiceImpl implements HistoricalPortfolioService {

    @Autowired
    HistoricalPortfolioRepository historicalPortfolioRepository;
    @Autowired
    UserService userService;
    @Autowired
    MarketDataService marketDataService;
    @Autowired
    PortfolioService portfolioService;

    @Override
    public void save() {
        Iterable<User> allUser = userService.getAllUsers();
        allUser.iterator()
                .forEachRemaining(user ->
                                historicalPortfolioRepository.save(new HistoricalPortfolioBuilder()
                                        .withUserId(user.getId())
                                        .withCash(user.getCash())
                                        .withPortfolio(portfolioService.getTotalPortfolioAmount(user))
                                        .build())
                );
    }
}
