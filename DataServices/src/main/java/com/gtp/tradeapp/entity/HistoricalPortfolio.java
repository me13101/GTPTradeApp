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

package com.gtp.tradeapp.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gtp.tradeapp.serialize.JsonDateDeserializer;
import com.gtp.tradeapp.serialize.JsonDateSerializer;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "historical_portfolio")

public class HistoricalPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long portfolioId;

    private Long userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @Column(name = "portfolio_date", insertable = true, nullable = false, updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime portfolioDate;

    @Column(name = "net_asset_value")
    private BigDecimal netAssetValue;
    private BigDecimal cash;
    private BigDecimal portfolio;


    public HistoricalPortfolio() {
    }

    public HistoricalPortfolio(HistoricalPortfolio historicalPortfolio) {
        super();
        this.portfolioId = historicalPortfolio.getPortfolioId();
        this.userId = historicalPortfolio.getUserId();
        this.cash = historicalPortfolio.getCash();
        this.portfolio = historicalPortfolio.getPortfolio();
        this.portfolioDate = historicalPortfolio.getPortfolioDate();
        this.netAssetValue = historicalPortfolio.getNetAssetValue();
    }

    public Long getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(Long portfolioId) {
        this.portfolioId = portfolioId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(BigDecimal portfolio) {
        this.portfolio = portfolio;
    }

    public DateTime getPortfolioDate() {
        return portfolioDate;
    }

    public void setNetAssetValue(BigDecimal netAssetValue) {
        this.netAssetValue = netAssetValue;
    }

    public BigDecimal getNetAssetValue() {
        return netAssetValue;
    }

    @PrePersist
    protected void onCreate() {
        portfolioDate = DateTime.now();
    }


    @PostPersist
    protected void afterCreate() {
        netAssetValue = getCash().add(getPortfolio());
    }
}