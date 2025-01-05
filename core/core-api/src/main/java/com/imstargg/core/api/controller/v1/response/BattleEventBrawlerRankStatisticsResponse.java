package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BattleEventBrawlerRankStatistics;

import java.util.Map;

public record BattleEventBrawlerRankStatisticsResponse(
        long brawlerBrawlStarsId,
        Map<Integer, Long> rankToCounts
) {

    public static BattleEventBrawlerRankStatisticsResponse of(BattleEventBrawlerRankStatistics stats) {
        return new BattleEventBrawlerRankStatisticsResponse(
                stats.brawlerBrawlStarsId(),
                stats.rankToCounts()
        );
    }
}
