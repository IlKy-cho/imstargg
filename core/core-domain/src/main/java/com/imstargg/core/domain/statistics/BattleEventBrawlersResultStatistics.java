package com.imstargg.core.domain.statistics;

import java.util.List;

public record BattleEventBrawlersResultStatistics(
        List<Long> brawlerBrawlStarsIds,
        int victoryCount,
        int defeatCount,
        int drawCount
) {
}
