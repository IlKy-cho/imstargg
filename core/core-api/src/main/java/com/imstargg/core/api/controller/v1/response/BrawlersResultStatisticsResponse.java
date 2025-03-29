package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BrawlerPairResultStatistics;

import java.util.List;

public record BrawlersResultStatisticsResponse(
        List<Long> brawlerIds,
        long totalBattleCount,
        double winRate,
        double pickRate
) {

    public static BrawlersResultStatisticsResponse of(BrawlerPairResultStatistics statistics) {
        return new BrawlersResultStatisticsResponse(
                statistics.brawlerIds().stream().map(BrawlStarsId::value).toList(),
                statistics.totalBattleCount(),
                statistics.winRate(),
                statistics.pickRate()
        );
    }
}
