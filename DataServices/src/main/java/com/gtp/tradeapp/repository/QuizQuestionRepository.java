package com.gtp.tradeapp.repository;


import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.entity.QuizQuestion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuizQuestionRepository extends CrudRepository<QuizQuestion, Long> {
    @Query("SELECT q FROM QuizQuestion q WHERE q.type = ?1 AND q.level = ?2")
    Iterable<QuizQuestion> getQuestions(AssetClass type, int level);

    Optional<QuizQuestion> findById(Long id);
}
