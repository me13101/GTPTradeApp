package com.gtp.tradeapp.repository;


import com.gtp.tradeapp.entity.Position;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PositionRepository extends CrudRepository<Position, Long> {

    Optional<List<Position>> findStockByUserId(Long userId);

    Optional<Position> findStockByUserIdAndStockId(Long userId, String StockId);
}
