package com.gtp.tradeapp.builder;


import com.gtp.tradeapp.entity.Transaction;

import java.math.BigDecimal;

public class TransactionBuilder {
    private Transaction transaction;

    public TransactionBuilder() {
        transaction = new Transaction();
    }

    public TransactionBuilder(Long id) {
        this();
        this.transaction.setTxnId(id);
    }

    public TransactionBuilder withUserId(Long userId) {
        this.transaction.setUserId(userId);
        return this;
    }

    public TransactionBuilder withTicker(String ticker) {
        this.transaction.setTicker(ticker);
        return this;
    }

    public TransactionBuilder withQuantity(int quantity) {
        this.transaction.setQty(quantity);
        return this;
    }

    public TransactionBuilder withPrice(BigDecimal price) {
        this.transaction.setPrice(price);
        return this;
    }

    public Transaction build() {
        return transaction;
    }
}
