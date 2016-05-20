package com.gtp.tradeapp.service.gamification;

import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.entity.Badge;

import java.util.Optional;

public interface BadgeService {

    Iterable<Badge> getBadgesByUser(Long userid);

    Optional<Badge> getBadgesByAssetClass(AssetClass type);

    Badge updateBadge(Badge b, int val);

}
