package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.Map;

public record BattleEventBrawlerRankStatistics(
        BrawlStarsId brawlerBrawlStarsId,
        Map<Integer, Long> rankToCounts
) {
}
