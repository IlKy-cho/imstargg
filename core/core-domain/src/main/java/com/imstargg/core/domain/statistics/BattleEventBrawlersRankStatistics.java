package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.List;
import java.util.Map;

public record BattleEventBrawlersRankStatistics(
        List<BrawlStarsId> brawlerBrawlStarsIds,
        Map<Integer, Long> rankToCounts
) {
}
