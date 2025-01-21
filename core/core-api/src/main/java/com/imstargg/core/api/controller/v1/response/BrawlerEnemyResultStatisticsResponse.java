package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BrawlerEnemyResultStatistics;

public record BrawlerEnemyResultStatisticsResponse(
        long brawlerId,
        long enemyBrawlerId,
        long totalBattleCount,
        double winRate,
        double pickRate
) {

    public static BrawlerEnemyResultStatisticsResponse of(BrawlerEnemyResultStatistics statistics) {
        return new BrawlerEnemyResultStatisticsResponse(
                statistics.brawlerId().value(),
                statistics.enemyBrawlerId().value(),
                statistics.totalBattleCount(),
                statistics.winRate(),
                statistics.pickRate()
        );
    }
}
