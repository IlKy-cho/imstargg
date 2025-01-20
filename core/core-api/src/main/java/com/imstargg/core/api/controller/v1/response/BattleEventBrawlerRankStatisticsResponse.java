package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BrawlerRankStatistics;

public record BattleEventBrawlerRankStatisticsResponse(
        long brawlerId,
        long totalBattleCount,
        double averageRank,
        double pickRate
) {

    public static BattleEventBrawlerRankStatisticsResponse of(BrawlerRankStatistics stats) {
        return new BattleEventBrawlerRankStatisticsResponse(
                stats.brawlerId().value(),
                stats.totalBattleCount(),
                stats.averageRank(),
                stats.pickRate()
        );
    }
}
