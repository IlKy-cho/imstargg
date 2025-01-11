package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BattleEventBrawlerResultStatistics;

public record BattleEventBrawlerResultStatisticsResponse(
        long brawlerId,
        long totalBattleCount,
        double winRate,
        double pickRate,
        double starPlayerRate
) {

    public static BattleEventBrawlerResultStatisticsResponse of(BattleEventBrawlerResultStatistics statistics) {
        return new BattleEventBrawlerResultStatisticsResponse(
                statistics.brawlerId().value(),
                statistics.totalBattleCount(),
                statistics.winRate(),
                statistics.pickRate(),
                statistics.starPlayerRate()
        );
    }
}
