package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BattleEventBrawlersResultStatistics;

import java.util.List;

public record BattleEventBrawlersResultStatisticsResponse(
        List<Long> brawlerBrawlStarsIds,
        int victoryCount,
        int defeatCount,
        int drawCount
) {

    public static BattleEventBrawlersResultStatisticsResponse of(BattleEventBrawlersResultStatistics statistics) {
        return new BattleEventBrawlersResultStatisticsResponse(
                statistics.brawlerBrawlStarsIds(),
                statistics.victoryCount(),
                statistics.defeatCount(),
                statistics.drawCount()
        );
    }
}
