package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.Application;
import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.util.IteratorUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class QuizQuestionRepositoryITest {

    @Autowired
    QuizQuestionRepository repository;

    @Test
    public void shouldReturnQuizQuestions() throws Exception {

        assertEquals(30, IteratorUtil.getSize(repository.findAll()));
        assertEquals(10, IteratorUtil.getSize(repository.getQuestions(AssetClass.Equity, 1)));
        assertEquals(0, IteratorUtil.getSize(repository.getQuestions(AssetClass.Equity, 2)));
        assertEquals(1, repository.findById(2L).get().getCorrectAns());
        assertEquals(1, repository.findById(2L).get().getCorrectAns());
    }
}