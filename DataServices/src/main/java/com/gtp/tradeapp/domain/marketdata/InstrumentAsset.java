package com.gtp.tradeapp.domain.marketdata;

import java.math.BigDecimal;

public class InstrumentAsset {
    private String name;
    private String type;
    private InstrumentData data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InstrumentData getData() {
        return data;
    }

    public BigDecimal getPrice() {
        return data.getPrice();
    }

    public String getDelta() {
        return data.getDelta();
    }

    public void setData(InstrumentData data) {
        this.data = data;
    }

    public InstrumentAsset() {

    }
}
