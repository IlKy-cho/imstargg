package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BattleEventBrawlerRankStatistics(
        BrawlStarsId brawlerId,
        long totalBattleCount,
        double averageRank,
        double pickRate
) {
}
