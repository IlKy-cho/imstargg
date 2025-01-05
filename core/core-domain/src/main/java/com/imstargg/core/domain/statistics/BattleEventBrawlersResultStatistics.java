package com.imstargg.core.domain.statistics;

import java.util.List;

public record BattleEventBrawlersResultStatistics(
        List<Long> brawlerBrawlStarsIds,
        long victoryCount,
        long defeatCount,
        long drawCount
) {
}
