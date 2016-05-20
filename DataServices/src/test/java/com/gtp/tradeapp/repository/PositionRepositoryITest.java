package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.Application;
import com.gtp.tradeapp.builder.PositionBuilder;
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
public class PositionRepositoryITest {
    @Autowired
    PositionRepository repository;

    @Test
    public void shouldReturnStock() {
        assertEquals(3, IteratorUtil.getSize(repository.findAll()));
    }

    @Test
    public void shouldReturnStockByUser() {
        assertEquals(2, IteratorUtil.getSize(repository.findStockByUserId(4L).get()));
    }

    @Test
    public void shouldReturnStockByUserAndStockId() {
        assertEquals(100, repository.findStockByUserIdAndStockId(4L, "UBS").get().getQuantity());
    }

    @Test
    public void shouldUpdatePosition() throws Exception {
        repository.save(new PositionBuilder()
                .withQuantity(10)
                .withStockId("STRL")
                .withUserId(1L)
                .build());

        assertEquals(4, IteratorUtil.getSize(repository.findAll()));

    }
}