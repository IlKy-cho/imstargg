package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlersRankStatistics;

import java.util.List;

public record BattleEventBrawlersRankStatisticsResponse(
        List<Long> brawlerIds,
        long totalBattleCount,
        double averageRank,
        double pickRate
) {

    public static BattleEventBrawlersRankStatisticsResponse of(BattleEventBrawlersRankStatistics stats) {
        return new BattleEventBrawlersRankStatisticsResponse(
                stats.brawlerIds().stream().map(BrawlStarsId::value).toList(),
                stats.totalBattleCount(),
                stats.averageRank(),
                stats.pickRate()
        );
    }
}
