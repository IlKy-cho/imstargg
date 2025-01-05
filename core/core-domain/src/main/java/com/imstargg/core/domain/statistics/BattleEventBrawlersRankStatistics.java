package com.imstargg.core.domain.statistics;

import java.util.List;
import java.util.Map;

public record BattleEventBrawlersRankStatistics(
        List<Long> brawlerBrawlStarsIds,
        Map<Integer, Long> rankToCounts
) {
}
