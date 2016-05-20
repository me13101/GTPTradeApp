package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.Application;
import com.gtp.tradeapp.entity.HistoricalPortfolio;
import com.gtp.tradeapp.util.IteratorUtil;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class HistoricalPortfolioRepositoryITest {

    @Autowired
    HistoricalPortfolioRepository repository;

    @Test
    public void addGamerRepository() {
        HistoricalPortfolio historicalPortfolio = new HistoricalPortfolio();

        historicalPortfolio.setUserId(1L);
        historicalPortfolio.setCash(BigDecimal.TEN);
        historicalPortfolio.setPortfolio(BigDecimal.ONE);

        repository.save(historicalPortfolio);

        assertEquals(1, IteratorUtil.getSize(repository.findByUserId(1L).get()));
        assertEquals(7, IteratorUtil.getSize(repository.findAll()));
        assertNotNull(repository.findByUserId(1L));
    }

    @Test
    public void deleteGamerRepository() {
        repository.delete(repository.findByUserId(1L).get());

        assertEquals(6, IteratorUtil.getSize(repository.findAll()));
    }

    @Test
    public void shouldListHistoryByOrder() {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        List<HistoricalPortfolio> historicalPortfolioList = repository.findByUserIdOrderByPortfolioDateDesc(4L).get();

        assertEquals(
                //TODO: Fix Time Zone Issue
                dateTimeFormatter.parseDateTime("2015-10-15 23:10:00"),
                historicalPortfolioList.get(0).getPortfolioDate()
        );

    }
}