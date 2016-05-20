package com.gtp.tradeapp.rest;


import com.gtp.tradeapp.domain.quiz.QuizAnswersList;
import com.gtp.tradeapp.entity.Badge;
import com.gtp.tradeapp.entity.Gamer;
import com.gtp.tradeapp.entity.QuizQuestion;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.service.gamification.BadgeService;
import com.gtp.tradeapp.service.gamification.GamerService;
import com.gtp.tradeapp.service.gamification.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/game")
public class GamificationController {
    private final BadgeService badgeService;
    private final GamerService gamerService;
    private final QuizService quizService;

    @Autowired
    public GamificationController(BadgeService badgeService, GamerService gamerService, QuizService quizService) {

        this.badgeService = badgeService;
        this.gamerService = gamerService;
        this.quizService = quizService;
    }

    @RequestMapping(value = "/gamer/{id}/badges", method = RequestMethod.POST)
    public Iterable<Badge> getUserBadges(@PathVariable("id") Long id) {
        return badgeService.getBadgesByUser(id);
    }

    @RequestMapping(value = "/gamer/badges")
    public Iterable<Badge> getUserBadges(@AuthenticationPrincipal User user) {
        return badgeService.getBadgesByUser(user.getId());
    }

    @RequestMapping(value = "/gamer/{id}", method = RequestMethod.POST)
    public Gamer getGamerProfile(@PathVariable("id") String id) {
        return gamerService.getGamerByUser(Long.valueOf(id)).get();
    }

    @RequestMapping(value = "/gamer/profile")
    public Gamer getGamerProfile(@AuthenticationPrincipal User user) {
        return gamerService.getGamerByUser(user.getId()).get();
    }

    @RequestMapping(value = "/leaderboard")
    public List<User> getLeaderboard() {
        return gamerService.getLeaderboard();
    }


    @RequestMapping(value = "/navleaderboard")
    public List<String[]> getNavLeaderboard() {
        return gamerService.getNavLeaderboard();

    }

    @RequestMapping(value = "/quiz/list/{type}/{level}")
    public List<QuizQuestion> getQuizList(@PathVariable("type") String type, @PathVariable("level") int level) {
        return quizService.getQuizQuestions(type, level);

    }

    @RequestMapping(value = "/quiz/mark/{type}/{level}", method = RequestMethod.POST, consumes = "application/json")
    public Map<String, Object> markQuiz(@AuthenticationPrincipal User user, @PathVariable("type") String type, @PathVariable("level") int level, @RequestBody QuizAnswersList quizAnswers) {

        return quizService.markQuiz(quizAnswers, type, level, user.getId());

    }


}
