/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gtp.tradeapp.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gtp.tradeapp.domain.Status;
import com.gtp.tradeapp.domain.marketdata.MarketInstrument;
import com.gtp.tradeapp.entity.Transaction;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.service.marketdata.MarketDataService;
import com.gtp.tradeapp.service.portfolio.PortfolioService;
import com.gtp.tradeapp.service.transaction.TransactionService;
import com.gtp.tradeapp.service.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@RestController
@RequestMapping("/trading")
public class TransactionController {
    private static final Logger LOGGER = Logger.getLogger(TransactionController.class);

    private final UserService userService;
    private final TransactionService transactionService;
    private final MarketDataService dataService;
    private final PortfolioService portfolioService;

    @Autowired
    public TransactionController(
            UserService userService,
            TransactionService transactionService,
            MarketDataService dataService,
            PortfolioService portfolio) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.dataService = dataService;
        this.portfolioService = portfolio;
    }

    @RequestMapping(value = "/buy", method = RequestMethod.POST, consumes = {"application/json; charset=UTF-8"})
    public Status postCreateBuyTransaction(@AuthenticationPrincipal User user,
                                           @RequestBody Transaction transaction) throws JsonProcessingException {
        LOGGER.debug("Creating new buy transaction={}" + transaction.asJsonString());
        checkArgument(transaction.getQty() > 0);
        //Updating Transaction With Latest Price
        Transaction updatedTransaction = updateWithLatestPrice(transaction.withUserId(user.getId()));
        if (validateIfEnoughCash(updatedTransaction)) {
            Transaction buy = transactionService.performAssetBuy(updatedTransaction);
            return new Status(1, "Buy Transaction added Successfully!", buy);
        } else {
            return new Status(0, "User does not have enough cash for the transaction");
        }
    }

    @RequestMapping(value = "/sell", method = RequestMethod.POST, consumes = {"application/json; charset=UTF-8"})
    public Status postCreateSellTransaction(@AuthenticationPrincipal User user,
                                            @RequestBody Transaction transaction) throws JsonProcessingException {
        LOGGER.debug("Creating new sell transaction={}" + transaction.asJsonString());
        checkArgument(transaction.getQty() > 0);
        //Updating Transaction With Latest Price
        Transaction updatedTransaction = updateWithLatestPrice(transaction.withUserId(user.getId()));
        if (validateIfEnoughPortfolio(updatedTransaction)) {
            Transaction sell = transactionService.performAssetSell(updatedTransaction);
            return new Status(1, "Sell Transaction added Successfully!", sell);
        } else {
            return new Status(0, "User does not have enough stocks for the transaction");
        }
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public Iterable<Transaction> getHistoryOfTransactions(@AuthenticationPrincipal User user,
                                                          @RequestParam(required = false, defaultValue = "50", value = "limit") int limit) {
        LOGGER.debug("Getting history of transactions={}");
        checkArgument(limit >= 0);

        return transactionService.getTransactionsByUser(user, limit);
    }

    private Transaction updateWithLatestPrice(Transaction transaction) {
        MarketInstrument latestStockPrice = dataService.getLatestStockPrice(transaction.getTicker());
        BigDecimal price = latestStockPrice.getStocklist().get(0).getPrice();

        transaction.setPrice(price);
        transaction.setTotal(price.multiply(BigDecimal.valueOf(transaction.getQty())));
        return transaction;
    }

    private boolean validateIfEnoughCash(Transaction transaction) {
        Optional<User> userDao = userService.getUserById(transaction.getUserId());
        if (userDao.isPresent()) {
            return userDao.get().getCash().compareTo(transaction.getTotal()) > 0;
        }
        return false;
    }

    private boolean validateIfEnoughPortfolio(Transaction updatedTransaction) {
        return portfolioService.checkForPosition(updatedTransaction);
    }
}
