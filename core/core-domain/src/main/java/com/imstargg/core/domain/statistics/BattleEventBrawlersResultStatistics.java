package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.List;

public record BattleEventBrawlersResultStatistics(
        List<BrawlStarsId> brawlerBrawlStarsIds,
        long totalBattleCount,
        double winRate,
        double pickRate
) {
}
