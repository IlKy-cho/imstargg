package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerPairRankStatistics(
        BrawlStarsId brawlerId,
        BrawlStarsId otherBrawlerId,
        long totalBattleCount,
        double averageRank,
        double pickRate
) {
}
