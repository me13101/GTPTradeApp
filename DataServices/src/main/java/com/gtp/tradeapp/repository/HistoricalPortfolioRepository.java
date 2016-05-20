package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.entity.HistoricalPortfolio;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface HistoricalPortfolioRepository extends CrudRepository<HistoricalPortfolio, Long> {
    Optional<List<HistoricalPortfolio>> findByUserId(Long userId);

    Optional<List<HistoricalPortfolio>> findByUserIdOrderByPortfolioDateDesc(Long userId);
}
