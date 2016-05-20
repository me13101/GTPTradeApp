package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.Application;
import com.gtp.tradeapp.entity.Gamer;
import com.gtp.tradeapp.util.IteratorUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class GamerRepositoryITest {

    @Autowired
    GamerRepository gamerRepository;

    @Test
    public void addGamerRepository() {
        Gamer gamer = new Gamer(1L);
        gamerRepository.save(gamer);

        assertEquals(6, IteratorUtil.getSize(gamerRepository.findAll()));
        assertNotNull(gamerRepository.findByUserIdIs(1L));
    }

    @Test
    public void deleteGamerRepository() {
        gamerRepository.delete(gamerRepository.findByUserIdIs(1L).get());

        assertEquals(5, IteratorUtil.getSize(gamerRepository.findAll()));
    }
}