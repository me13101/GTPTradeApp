package com.gtp.tradeapp.service.transaction;


import com.gtp.tradeapp.entity.Transaction;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.repository.TransactionRepository;
import com.gtp.tradeapp.service.portfolio.PortfolioService;
import com.gtp.tradeapp.service.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.min;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger LOGGER = Logger.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PortfolioService portfolioService;

    @Override
    public Iterable<Transaction> getAllTransactions() {
        LOGGER.debug("Getting all transactions");
        return transactionRepository.findAll();
    }

    @Override
    public Iterable<Transaction> getTransactionsByUser(User user, int limit) {
        LOGGER.debug("Getting transactions by user");

        Optional<List<Transaction>> history = transactionRepository.findByUserId(user.getId());
        if (history.isPresent()) {
            return history.get().subList(0, min(history.get().size(), limit));
        }
        return null;
    }

    @Override
    public Transaction performAssetBuy(Transaction newTransaction) {
        LOGGER.debug("Creating transaction={}" + newTransaction.getTxnId());

        return performAssetAction(newTransaction);
    }

    @Override
    public Transaction performAssetSell(Transaction newTransaction) {
        LOGGER.debug("Creating transaction={}" + newTransaction.getTxnId());

        newTransaction.setQty(Math.negateExact(newTransaction.getQty()));
        newTransaction.setTotal(newTransaction.getTotal().negate());

        return performAssetAction(newTransaction);
    }

    private Transaction performAssetAction(Transaction newTransaction) {
        Optional<User> userDao = userService.getUserById(newTransaction.getUserId());

        if (userDao.isPresent()) {
            User user = userDao.get();
            user.addCash(newTransaction.getTotal().negate());
            portfolioService.updatePosition(newTransaction);
            userService.update(user);
            return transactionRepository.save(newTransaction);
        }

        return null;
    }

}
