package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u.firstname, u.lastname, (u.cash-1000000) as Profit, u.id FROM User u ORDER BY Profit DESC")
    Iterable<User> getLeaderboard();
}
