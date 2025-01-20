package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BrawlerEnemyResultCount(
        BrawlStarsId brawlerId,
        BrawlStarsId enemyBrawlerId,
        ResultCount resultCount
) {
}
