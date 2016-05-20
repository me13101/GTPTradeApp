package com.gtp.tradeapp.service.transaction;

import com.gtp.tradeapp.entity.Transaction;
import com.gtp.tradeapp.entity.User;

public interface TransactionService {

    Iterable<Transaction> getAllTransactions();

    Iterable<Transaction> getTransactionsByUser(User user, int limit);

    Transaction performAssetBuy(Transaction transaction);

    Transaction performAssetSell(Transaction transaction);
}