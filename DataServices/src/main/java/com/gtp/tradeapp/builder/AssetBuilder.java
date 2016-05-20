package com.gtp.tradeapp.builder;


import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.domain.AssetType;
import com.gtp.tradeapp.entity.Asset;

public class AssetBuilder {
    private Asset asset;

    public AssetBuilder() {
        asset = new Asset();
    }

    public AssetBuilder withName(String name) {
        this.asset.setName(name);
        return this;
    }

    public AssetBuilder withAssetClass(AssetClass assetClass) {
        this.asset.setAssetClass(assetClass);
        return this;
    }

    public AssetBuilder withIndustry(String industry) {
        this.asset.setIndustry(industry);
        return this;
    }

    public AssetBuilder withRegion(String region) {
        this.asset.setRegion(region);
        return this;
    }

    public AssetBuilder withTicker(String ticker) {
        this.asset.setTicker(ticker);
        return this;
    }

    public AssetBuilder withAssetType(AssetType assetType) {
        this.asset.setType(assetType);
        return this;
    }

    public Asset build() {
        return asset;
    }
}
