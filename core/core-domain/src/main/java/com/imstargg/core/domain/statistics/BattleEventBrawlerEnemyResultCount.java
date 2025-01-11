package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BattleEventBrawlerEnemyResultCount(
        BrawlStarsId brawlerId,
        BrawlStarsId enemyBrawlerId,
        long victoryCount,
        long defeatCount,
        long drawCount
) {
}
