package com.imstargg.core.domain.statistics;

import java.util.Map;

public record BattleEventBrawlerRankStatistics(
        long brawlerBrawlStarsId,
        Map<Integer, Long> rankToCounts
) {
}
