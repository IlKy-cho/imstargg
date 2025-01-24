package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.BattleEventResultStatistics;

public record BattleEventResultStatisticsResponse(
        BattleEventResponse event,
        long totalBattleCount,
        double winRate,
        double starPlayerRate
) {

    public static BattleEventResultStatisticsResponse of(BattleEventResultStatistics statistics) {
        return new BattleEventResultStatisticsResponse(
                BattleEventResponse.from(statistics.event()),
                statistics.totalBattleCount(),
                statistics.winRate(),
                statistics.starPlayerRate()
        );
    }
}
