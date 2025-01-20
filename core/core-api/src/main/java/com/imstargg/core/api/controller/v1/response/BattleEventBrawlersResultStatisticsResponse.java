package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BrawlersResultStatistics;

import java.util.List;

public record BattleEventBrawlersResultStatisticsResponse(
        List<Long> brawlerIds,
        long totalBattleCount,
        double winRate,
        double pickRate
) {

    public static BattleEventBrawlersResultStatisticsResponse of(BrawlersResultStatistics statistics) {
        return new BattleEventBrawlersResultStatisticsResponse(
                statistics.brawlerIds().stream().map(BrawlStarsId::value).toList(),
                statistics.totalBattleCount(),
                statistics.winRate(),
                statistics.pickRate()
        );
    }
}
