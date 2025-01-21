package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerEnemyResultStatistics(
        BrawlStarsId brawlerId,
        BrawlStarsId enemyBrawlerId,
        long totalBattleCount,
        double winRate,
        double pickRate
) {
}
