package com.gtp.tradeapp.entity;


import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.domain.AssetType;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "asset_table")
public class Asset {

    @Id
    @NotEmpty
    @Column(unique = true, nullable = false)
    private String assetId;

    @NotEmpty
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "assetclass")
    private AssetClass assetClass;

    @Enumerated(EnumType.STRING)
    private AssetType type;

    @NotEmpty
    private String industry;

    @NotEmpty
    private String region;

    public Asset() {
    }

    public Asset(String assetId, String name, AssetClass assetClass, AssetType type, String industry, String region) {
        this.assetId = assetId;
        this.name = name;
        this.assetClass = assetClass;
        this.type = type;
        this.industry = industry;
        this.region = region;
    }

    public AssetClass getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(AssetClass assetClass) {
        this.assetClass = assetClass;
    }

    public String getTicker() {
        return assetId;
    }

    public String getName() {
        return name;
    }

    public AssetType getType() {
        return type;
    }

    public String getIndustry() {
        return industry;
    }

    public String getRegion() {
        return region;
    }

    public void setTicker(String stockID) {
        this.assetId = stockID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
