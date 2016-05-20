package com.gtp.tradeapp.entity;

import com.gtp.tradeapp.domain.AssetClass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class QuizQuestion {
    @Id
    private Long id;

    private int level;

    @Column(length = 65535, columnDefinition = "VARCHAR(8000)")
    private String question;

    @Enumerated(EnumType.STRING)
    private AssetClass type;

    @Column(length = 65535, columnDefinition = "VARCHAR(8000)")
    private String a1;

    @Column(length = 65535, columnDefinition = "VARCHAR(8000)")
    private String a2;

    @Column(length = 65535, columnDefinition = "VARCHAR(8000)")
    private String a3;


    private int correctAns;

    public QuizQuestion() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public AssetClass getType() {
        return type;
    }

    public void setType(AssetClass type) {
        this.type = type;
    }

    public String getA1() {
        return a1;
    }

    public void setA1(String a1) {
        this.a1 = a1;
    }

    public String getA2() {
        return a2;
    }

    public void setA2(String a2) {
        this.a2 = a2;
    }

    public String getA3() {
        return a3;
    }

    public void setA3(String a3) {
        this.a3 = a3;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }
}
