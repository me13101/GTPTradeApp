package com.gtp.tradeapp.domain.marketdata;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gtp.tradeapp.serialize.JsonDateDeserializer;
import com.gtp.tradeapp.serialize.JsonDateSerializer;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class InstrumentData {
    private String delta;
    private String stock_id;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private DateTime datetime;
    private BigDecimal price;
    private BigDecimal pricechange;

    public InstrumentData() {
    }

    public String getDelta() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

    public String getStock_id() {
        return stock_id;
    }

    public void setStock_id(String stock_id) {
        this.stock_id = stock_id;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(DateTime datetime) {
        this.datetime = datetime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPricechange() {
        return pricechange;
    }

    public void setPricechange(BigDecimal pricechange) {
        this.pricechange = pricechange;
    }
}
