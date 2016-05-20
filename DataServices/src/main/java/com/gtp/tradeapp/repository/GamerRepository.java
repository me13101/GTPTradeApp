package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.entity.Gamer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GamerRepository extends CrudRepository<Gamer, Long> {

    Optional<Gamer> findByUserIdIs(Long userId);

}
