package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BattleEventBrawlerResultStatistics;

import java.time.LocalDate;

public record BattleEventBrawlerResultStatisticsResponse(
        long brawlerBrawlStarsId,
        LocalDate battleDate,
        int victoryCount,
        int defeatCount,
        int drawCount,
        int starPlayerCount
) {

    public static BattleEventBrawlerResultStatisticsResponse of(BattleEventBrawlerResultStatistics statistics) {
        return new BattleEventBrawlerResultStatisticsResponse(
                statistics.brawlerBrawlStarsId(),
                statistics.battleDate(),
                statistics.victoryCount(),
                statistics.defeatCount(),
                statistics.drawCount(),
                statistics.starPlayerCount()
        );
    }
}
