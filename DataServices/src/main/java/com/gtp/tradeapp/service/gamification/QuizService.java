package com.gtp.tradeapp.service.gamification;

import com.gtp.tradeapp.domain.quiz.QuizAnswersList;
import com.gtp.tradeapp.entity.QuizQuestion;

import java.util.List;
import java.util.Map;

public interface QuizService {
    List<QuizQuestion> getQuizQuestions(String type, int level);

    Map<String, Object> markQuiz(QuizAnswersList quizAnswers, String type, int level, Long userId);
}
