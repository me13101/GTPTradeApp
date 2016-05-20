package com.gtp.tradeapp.builder;


import com.gtp.tradeapp.entity.Position;

public class PositionBuilder {
    private Position position;

    public PositionBuilder() {
        position = new Position();
    }

    public PositionBuilder withQuantity(int qty) {
        this.position.setQuantity(qty);
        return this;
    }

    public PositionBuilder withUserId(Long userId) {
        this.position.setUserId(userId);
        return this;
    }

    public PositionBuilder withStockId(String stockId) {
        this.position.setStockId(stockId);
        return this;
    }

    public Position build() {
        return position;
    }
}
