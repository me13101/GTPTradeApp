package com.gtp.tradeapp.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.serialize.JsonDateDeserializer;
import com.gtp.tradeapp.serialize.JsonDateSerializer;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(QuizHistoryPK.class)
public class QuizHistory {

    @Id
    private Long userId;

    @Id
    @Enumerated(EnumType.STRING)
    private AssetClass type;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @Column(name = "datetime", insertable = true, nullable = false, updatable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime datetime;

    private String lastScore;

    public QuizHistory() {

    }

    public QuizHistory(Long userId, AssetClass type, DateTime datetime, String lastScore) {
        this.userId = userId;
        this.type = type;
        this.datetime = datetime;
        this.lastScore = lastScore;
    }

    public Long getUserId() {
        return userId;
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

    public DateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(DateTime datetime) {
        this.datetime = datetime;
    }

    public String getLastScore() {
        return lastScore;
    }

    public void setLastScore(String lastScore) {
        this.lastScore = lastScore;
    }
}
