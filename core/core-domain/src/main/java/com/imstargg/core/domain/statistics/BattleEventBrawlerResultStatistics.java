package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BattleEventBrawlerResultStatistics(
        BrawlStarsId brawlerId,
        long totalBattleCount,
        double winRate,
        double pickRate,
        double starPlayerRate
) {
}
