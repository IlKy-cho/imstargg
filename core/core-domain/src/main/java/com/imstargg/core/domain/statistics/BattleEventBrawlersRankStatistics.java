package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.List;

public record BattleEventBrawlersRankStatistics(
        List<BrawlStarsId> brawlerIds,
        long totalBattleCount,
        double averageRank,
        double pickRate
) {
}
