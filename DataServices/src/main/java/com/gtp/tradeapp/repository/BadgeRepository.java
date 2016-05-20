package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.entity.Badge;
import org.springframework.data.repository.CrudRepository;

public interface BadgeRepository extends CrudRepository<Badge, Long> {

    Iterable<Badge> findByUserIdIs(Long userId);
}
