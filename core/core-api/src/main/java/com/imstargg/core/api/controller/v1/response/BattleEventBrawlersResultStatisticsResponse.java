package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlersResultStatistics;

import java.util.List;

public record BattleEventBrawlersResultStatisticsResponse(
        List<Long> brawlerBrawlStarsIds,
        long totalBattleCount,
        double winRate,
        double pickRate
) {

    public static BattleEventBrawlersResultStatisticsResponse of(BattleEventBrawlersResultStatistics statistics) {
        return new BattleEventBrawlersResultStatisticsResponse(
                statistics.brawlerIds().stream().map(BrawlStarsId::value).toList(),
                statistics.totalBattleCount(),
                statistics.winRate(),
                statistics.pickRate()
        );
    }
}
