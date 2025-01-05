package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlersRankStatistics;

import java.util.List;
import java.util.Map;

public record BattleEventBrawlersRankStatisticsResponse(
        List<Long> brawlerBrawlStarsIds,
        Map<Integer, Long> rankToCounts
) {

    public static BattleEventBrawlersRankStatisticsResponse of(BattleEventBrawlersRankStatistics stats) {
        return new BattleEventBrawlersRankStatisticsResponse(
                stats.brawlerBrawlStarsIds().stream().map(BrawlStarsId::value).toList(),
                stats.rankToCounts()
        );
    }
}
