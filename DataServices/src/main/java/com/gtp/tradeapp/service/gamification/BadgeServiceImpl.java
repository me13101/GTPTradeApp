package com.gtp.tradeapp.service.gamification;

import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.entity.Badge;
import com.gtp.tradeapp.repository.BadgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepo;

    @Autowired
    public BadgeServiceImpl(BadgeRepository badgeRepo) {
        this.badgeRepo = badgeRepo;
    }

    @Override
    public Iterable<Badge> getBadgesByUser(Long userid) {
        return badgeRepo.findByUserIdIs(userid);
    }

    @Override
    public Optional<Badge> getBadgesByAssetClass(AssetClass type) {

        Badge b = new Badge(1L, type, 1);
        return Optional.of(b);
    }

    @Override
    public Badge updateBadge(Badge b, int val) {
        b.setLevel(b.getLevel() + val);
        return b;
    }
}
