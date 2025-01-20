package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BrawlerResultStatistics;

public record BattleEventBrawlerResultStatisticsResponse(
        long brawlerId,
        long totalBattleCount,
        double winRate,
        double pickRate,
        double starPlayerRate
) {

    public static BattleEventBrawlerResultStatisticsResponse of(BrawlerResultStatistics statistics) {
        return new BattleEventBrawlerResultStatisticsResponse(
                statistics.brawlerId().value(),
                statistics.totalBattleCount(),
                statistics.winRate(),
                statistics.pickRate(),
                statistics.starPlayerRate()
        );
    }
}
