package com.gtp.tradeapp.repository;

import com.gtp.tradeapp.Application;
import com.gtp.tradeapp.domain.AssetType;
import com.gtp.tradeapp.entity.Asset;
import com.gtp.tradeapp.util.IteratorUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class AssetRepositoryITest {

    @Autowired
    AssetRepository assetRepository;

    @Test
    public void ReturnAllAsset() {
        Iterable<Asset> all = assetRepository.findAll();

        assertEquals(41, IteratorUtil.getSize(all));
        assertEquals(36, StreamSupport.stream(all.spliterator(), false)
                .filter(s -> s.getType() == AssetType.stock)
                .count());
        assertEquals(5, StreamSupport.stream(all.spliterator(), false)
                .filter(s -> s.getType() == AssetType.index)
                .count());
    }
}