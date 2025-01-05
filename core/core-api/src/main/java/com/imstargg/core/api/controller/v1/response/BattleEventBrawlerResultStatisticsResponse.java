package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BattleEventBrawlerResultStatistics;

public record BattleEventBrawlerResultStatisticsResponse(
        long brawlerBrawlStarsId,
        long victoryCount,
        long defeatCount,
        long drawCount,
        long starPlayerCount
) {

    public static BattleEventBrawlerResultStatisticsResponse of(BattleEventBrawlerResultStatistics statistics) {
        return new BattleEventBrawlerResultStatisticsResponse(
                statistics.brawlerBrawlStarsId(),
                statistics.victoryCount(),
                statistics.defeatCount(),
                statistics.drawCount(),
                statistics.starPlayerCount()
        );
    }
}
