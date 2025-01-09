package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;

public record BattleEventBrawlerResultCount(
        BrawlStarsId brawlerBrawlStarsId,
        long victoryCount,
        long defeatCount,
        long drawCount,
        long starPlayerCount
) {
}
