package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.Application;
import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.entity.Badge;
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
public class BadgeRepositoryITest {

    @Autowired
    BadgeRepository badgeRepository;

    @Test
    public void addBadgeRepository() {
        Badge badge = new Badge(1L, AssetClass.Commodities, 1);

        badgeRepository.save(badge);

        assertEquals(1, IteratorUtil.getSize(badgeRepository.findAll()));
        assertNotNull(badgeRepository.findByUserIdIs(1L));
    }

    @Test
    public void deleteBadgeRepository() {
        badgeRepository.delete(badgeRepository.findByUserIdIs(1L));

        assertEquals(0, IteratorUtil.getSize(badgeRepository.findAll()));
    }
}