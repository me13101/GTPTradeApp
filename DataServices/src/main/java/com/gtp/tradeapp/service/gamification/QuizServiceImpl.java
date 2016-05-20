package com.gtp.tradeapp.service.gamification;

import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.domain.quiz.QuizAnswer;
import com.gtp.tradeapp.domain.quiz.QuizAnswersList;
import com.gtp.tradeapp.entity.Gamer;
import com.gtp.tradeapp.entity.QuizQuestion;
import com.gtp.tradeapp.repository.QuizQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class QuizServiceImpl implements QuizService {
    private final QuizQuestionRepository quizRepo;
    private final GamerService gamerService;

    @Autowired
    public QuizServiceImpl(QuizQuestionRepository quizRepo, GamerService gamerService) {
        this.quizRepo = quizRepo;
        this.gamerService = gamerService;
    }


    @Override
    public List<QuizQuestion> getQuizQuestions(String type, int level) {
        List<QuizQuestion> quiz = (List<QuizQuestion>) quizRepo.getQuestions(AssetClass.valueOf(type), level);
        Collections.shuffle(quiz, new Random(System.nanoTime()));

        for (QuizQuestion q : quiz) {
            q.setCorrectAns(0);
        }
        return quiz.subList(0, 10);
    }

    @Override
    public Map<String, Object> markQuiz(QuizAnswersList quizAnswers, String type, int level, Long userId) {
        int score = 0;

        for (QuizAnswer quizAnswer : quizAnswers.getAnswerList()) {
            QuizQuestion qq = quizRepo.findById(quizAnswer.getId()).get();
            if (qq.getCorrectAns() == quizAnswer.getAnswer()) {
                score++;
                quizAnswer.setAnswer(1);
            } else {
                quizAnswer.setAnswer(0);
            }
        }

        Gamer g = gamerService.getGamerByUser(userId).get();
        try {
            gamerService.updateGamer(g, "chips", score * 10);
        } catch (Exception e) {
        }


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("score", score);
        hashMap.put("answerList", quizAnswers.getAnswerList());
        return hashMap;
    }
}
