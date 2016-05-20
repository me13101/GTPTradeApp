package com.gtp.tradeapp.service.gamification;

import com.gtp.tradeapp.entity.Gamer;
import com.gtp.tradeapp.entity.User;

import java.util.List;
import java.util.Optional;

public interface GamerService {
    Optional<Gamer> getGamerByUser(Long id);

    int updateGamer(Gamer gg, String col, int val) throws Exception;

    List<User> getLeaderboard();

    List<String[]> getNavLeaderboard();
}
