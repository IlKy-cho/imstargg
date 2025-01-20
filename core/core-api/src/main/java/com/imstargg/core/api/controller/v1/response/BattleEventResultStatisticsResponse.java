package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BattleEventResultStatistics;

public record BattleEventResultStatisticsResponse(
        long eventId,
        long totalBattleCount,
        double winRate,
        double starPlayerRate
) {

    public static BattleEventResultStatisticsResponse of(BattleEventResultStatistics statistics) {
        return new BattleEventResultStatisticsResponse(
                statistics.eventId().value(),
                statistics.totalBattleCount(),
                statistics.winRate(),
                statistics.starPlayerRate()
        );
    }
}
