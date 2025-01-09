package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BattleEventBrawlerResultCount;

public record BattleEventBrawlerResultStatisticsResponse(
        long brawlerBrawlStarsId,
        long victoryCount,
        long defeatCount,
        long drawCount,
        long starPlayerCount
) {

    public static BattleEventBrawlerResultStatisticsResponse of(BattleEventBrawlerResultCount statistics) {
        return new BattleEventBrawlerResultStatisticsResponse(
                statistics.brawlerBrawlStarsId().value(),
                statistics.victoryCount(),
                statistics.defeatCount(),
                statistics.drawCount(),
                statistics.starPlayerCount()
        );
    }
}
