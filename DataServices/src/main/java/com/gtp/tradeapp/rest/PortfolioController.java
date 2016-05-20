package com.gtp.tradeapp.rest;

import com.gtp.tradeapp.domain.CurrentPosition;
import com.gtp.tradeapp.entity.HistoricalPortfolio;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.service.portfolio.PortfolioService;
import com.gtp.tradeapp.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    @Autowired
    PortfolioService portfolioService;
    @Autowired
    UserService userService;

    @Deprecated
    @RequestMapping(value = "/amount", produces = "application/json;charset=UTF-8")
    public HashMap<String, Object> getPortfolioAmountFor(@AuthenticationPrincipal User user, @RequestParam(value = "username") String username) {
        return new HashMap<String, Object>() {{
            put("username", username);
            put("amount", portfolioService.getCurrentCashAmount(user));
        }};
    }

    @RequestMapping(value = "/details", produces = "application/json;charset=UTF-8")
    public HashMap<String, Object> getPortfolioStockListFor(@AuthenticationPrincipal User user) {
        List<CurrentPosition> currentPositionList = portfolioService.getCurrentPosition(user);

        return new HashMap<String, Object>() {{
            put("user", userService.basicUserInfo(user));
            put("summary", portfolioService.getAssetSummary(user));
            if (!currentPositionList.isEmpty()) {
                put("portfolio", currentPositionList);
                put("assetTypeDistribution", portfolioService.getAssetTypeDistribution(currentPositionList));
                put("assetClassDistribution", portfolioService.getAssetClassDistribution(currentPositionList));
            }
        }};
    }

    @RequestMapping(value = "/history/details", produces = "application/json;charset=UTF-8")
    public List<HistoricalPortfolio> getPortfolioHistory(@AuthenticationPrincipal User user) {
        return portfolioService.getHistoricalPortfolio(user);
    }

}

