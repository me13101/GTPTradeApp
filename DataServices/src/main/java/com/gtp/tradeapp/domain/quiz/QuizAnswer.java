package com.gtp.tradeapp.domain.quiz;


import java.io.Serializable;

public class QuizAnswer implements Serializable {
    private Long id;
    private int answer;

    public QuizAnswer() {

    }

    public QuizAnswer(Long id, int answer) {
        this.id = id;
        this.answer = answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
