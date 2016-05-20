package com.gtp.tradeapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long txnId;

    //TODO: Validations on parameters

    //@NotNull
    private Long userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @Column(name = "datetime", insertable = true, nullable = false, updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime datetime;

    //@NotNull
    private String ticker;

    //@NotNull
    private int qty;

    //@NotNull
    private BigDecimal price;

    //@NotNull
    private BigDecimal total;

    public Transaction() {
    }

    public Transaction(Transaction transaction) {
        super();
        this.txnId = transaction.getTxnId();
        this.userId = transaction.getUserId();
        this.ticker = transaction.getTicker();
        this.datetime = transaction.getDatetime();
        this.qty = transaction.getQty();
        this.price = transaction.getPrice();
        this.total = transaction.getTotal();

    }

    public Transaction withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getTxnId() {
        return txnId;
    }

    public void setTxnId(Long txnId) {
        this.txnId = txnId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public DateTime getDatetime() {
        return datetime;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotal() {
        return total;
    }

    @JsonIgnore
    public String asJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    @PrePersist
    protected void onCreate() {
        datetime = DateTime.now();
    }

    @PostPersist
    protected void afterCreate() {
        total = getPrice().multiply(BigDecimal.valueOf(getQty()));
    }
}

