package com.gtp.tradeapp.entity;

import java.io.Serializable;

class PositionPK implements Serializable {

    protected long userId;
    protected String stockId;

    public PositionPK() {
    }

    public PositionPK(long userId, String stockId) {
        this.userId = userId;
        this.stockId = stockId;
    }

}