package com.gtp.tradeapp.domain.quiz;

import java.util.List;


public class QuizAnswersList {
    private List<QuizAnswer> answerList;

    public QuizAnswersList() {

    }

    public List<QuizAnswer> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<QuizAnswer> answerList) {
        this.answerList = answerList;
    }

    public QuizAnswersList(List<QuizAnswer> answerList) {

        this.answerList = answerList;
    }
}