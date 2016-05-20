package com.gtp.tradeapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Gamer {

    @Id
    @Column(unique = true)
    private Long userId;
    private int chips;
    private int exp;
    private BigDecimal addMoney;

    public Gamer() {
    }

    public Gamer(Long userId) {
        this.userId = userId;
        this.chips = 0;
        this.exp = 0;
        this.addMoney = BigDecimal.valueOf(0);
    }


    public Gamer(Long userId, int chips, int exp, int addMoney) {
        this.userId = userId;
        this.chips = chips;
        this.exp = exp;
        this.addMoney = BigDecimal.valueOf(addMoney);
    }

    public int getChips() {
        return this.chips;
    }

    public int getExp() {
        return this.exp;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setChips(int chips) {
        this.chips = chips;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public BigDecimal getAddMoney() {
        return addMoney;
    }

    public void setAddMoney(BigDecimal addMoney) {
        this.addMoney = addMoney;
    }
}
