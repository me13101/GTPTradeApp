package com.gtp.tradeapp.service.transaction;

import com.gtp.tradeapp.entity.Transaction;
import com.gtp.tradeapp.entity.User;

import java.util.Collections;

//@Service
public class TransactionServiceStub implements TransactionService {

    //@Override
    //public BigDecimal getTransaction(User user) {
    // return new BigDecimal(1000000);
    //}

    @Override
    public Iterable<Transaction> getAllTransactions() {
        Transaction transaction = new Transaction();
        return Collections.singletonList(transaction);
    }

    @Override
    public Transaction performAssetBuy(Transaction transaction) {
        return transaction;
    }

    @Override
    public Transaction performAssetSell(Transaction transaction) {
        return null;
    }

    @Override
    public Iterable<Transaction> getTransactionsByUser(User user, int limit) {
        return null;
    }
}
