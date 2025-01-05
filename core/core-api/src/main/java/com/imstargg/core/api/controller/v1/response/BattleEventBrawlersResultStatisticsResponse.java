package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlersResultStatistics;

import java.util.List;

public record BattleEventBrawlersResultStatisticsResponse(
        List<Long> brawlerBrawlStarsIds,
        long victoryCount,
        long defeatCount,
        long drawCount
) {

    public static BattleEventBrawlersResultStatisticsResponse of(BattleEventBrawlersResultStatistics statistics) {
        return new BattleEventBrawlersResultStatisticsResponse(
                statistics.brawlerBrawlStarsIds().stream().map(BrawlStarsId::value).toList(),
                statistics.victoryCount(),
                statistics.defeatCount(),
                statistics.drawCount()
        );
    }
}
