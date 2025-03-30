package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BrawlerPairResultStatistics;

public record BrawlerPairResultStatisticsResponse(
        long brawlerId,
        long pairBrawlerId,
        long totalBattleCount,
        double winRate,
        double pickRate
) {

    public static BrawlerPairResultStatisticsResponse of(BrawlerPairResultStatistics statistics) {
        return new BrawlerPairResultStatisticsResponse(
                statistics.brawlerId().value(),
                statistics.otherBrawlerId().value(),
                statistics.totalBattleCount(),
                statistics.winRate(),
                statistics.pickRate()
        );
    }
}
