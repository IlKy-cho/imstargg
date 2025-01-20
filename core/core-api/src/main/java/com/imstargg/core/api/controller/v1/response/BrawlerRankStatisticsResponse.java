package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BrawlerRankStatistics;

public record BrawlerRankStatisticsResponse(
        long brawlerId,
        long totalBattleCount,
        double averageRank,
        double pickRate
) {

    public static BrawlerRankStatisticsResponse of(BrawlerRankStatistics stats) {
        return new BrawlerRankStatisticsResponse(
                stats.brawlerId().value(),
                stats.totalBattleCount(),
                stats.averageRank(),
                stats.pickRate()
        );
    }
}
