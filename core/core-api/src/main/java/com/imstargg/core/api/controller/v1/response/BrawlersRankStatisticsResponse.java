package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BrawlerPairRankStatistics;

import java.util.List;

public record BrawlersRankStatisticsResponse(
        List<Long> brawlerIds,
        long totalBattleCount,
        double averageRank,
        double pickRate
) {

    public static BrawlersRankStatisticsResponse of(BrawlerPairRankStatistics stats) {
        return new BrawlersRankStatisticsResponse(
                stats.brawlerIds().stream().map(BrawlStarsId::value).toList(),
                stats.totalBattleCount(),
                stats.averageRank(),
                stats.pickRate()
        );
    }
}
