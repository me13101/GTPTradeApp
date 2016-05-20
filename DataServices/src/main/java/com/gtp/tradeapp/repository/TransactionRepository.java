package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    Optional<List<Transaction>> findByUserId(Long userId);

}
