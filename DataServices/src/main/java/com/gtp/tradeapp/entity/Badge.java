package com.gtp.tradeapp.entity;

import com.gtp.tradeapp.domain.AssetClass;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Entity
@IdClass(BadgePK.class)
public class Badge implements Serializable {

    private static final long serialVersionUID = -1154422253287504930L;
    @Id
    private Long userId;
    @Id
    private AssetClass type;

    private int level;

    public Badge() {
    }

    public Badge(Long userId, AssetClass type, int level) {
        this.userId = userId;
        this.type = type;
        this.level = level;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public AssetClass getType() {
        return type;
    }

    public void setType(AssetClass type) {
        this.type = type;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setUser(Long userId) {
        this.userId = userId;
    }
} 