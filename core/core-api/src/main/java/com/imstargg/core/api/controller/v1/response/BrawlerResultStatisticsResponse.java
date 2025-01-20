package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BrawlerResultStatistics;

public record BrawlerResultStatisticsResponse(
        long brawlerId,
        long totalBattleCount,
        double winRate,
        double pickRate,
        double starPlayerRate
) {

    public static BrawlerResultStatisticsResponse of(BrawlerResultStatistics statistics) {
        return new BrawlerResultStatisticsResponse(
                statistics.brawlerId().value(),
                statistics.totalBattleCount(),
                statistics.winRate(),
                statistics.pickRate(),
                statistics.starPlayerRate()
        );
    }
}
