package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerRankStatistics(
        BrawlStarsId brawlerId,
        long totalBattleCount,
        double averageRank,
        double pickRate
) {
}
