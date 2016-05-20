package com.gtp.tradeapp.domain;

import com.gtp.tradeapp.entity.Asset;
import com.gtp.tradeapp.entity.Position;

import java.math.BigDecimal;

public class CurrentPosition extends Position {

    private BigDecimal currentPrice;
    private Double beta;
    private Asset asset;
    private BigDecimal totalAmount;

    public CurrentPosition(Position position) {
        super(position);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Double getBeta() {
        return beta;
    }

    public void setBeta(Double beta) {
        this.beta = beta;
    }
}
