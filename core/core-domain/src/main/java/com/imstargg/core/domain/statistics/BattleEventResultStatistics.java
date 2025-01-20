package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BattleEventResultStatistics(
        BrawlStarsId eventId,
        long totalBattleCount,
        double winRate,
        double starPlayerRate
) {
}
