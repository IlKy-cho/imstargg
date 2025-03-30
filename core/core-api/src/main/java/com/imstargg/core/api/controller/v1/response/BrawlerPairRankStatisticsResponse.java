package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BrawlerPairRankStatistics;

public record BrawlerPairRankStatisticsResponse(
        long brawlerId,
        long pairBrawlerId,
        long totalBattleCount,
        double averageRank,
        double pickRate
) {

    public static BrawlerPairRankStatisticsResponse of(BrawlerPairRankStatistics stats) {
        return new BrawlerPairRankStatisticsResponse(
                stats.brawlerId().value(),
                stats.otherBrawlerId().value(),
                stats.totalBattleCount(),
                stats.averageRank(),
                stats.pickRate()
        );
    }
}
