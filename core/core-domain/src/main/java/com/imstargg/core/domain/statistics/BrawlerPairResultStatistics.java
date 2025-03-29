package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerPairResultStatistics(
        BrawlStarsId brawlerId,
        BrawlStarsId otherBrawlerId,
        long totalBattleCount,
        double winRate,
        double pickRate
) {
}
