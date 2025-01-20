package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerResultStatistics(
        BrawlStarsId brawlerId,
        long totalBattleCount,
        double winRate,
        double pickRate,
        double starPlayerRate
) {
}
