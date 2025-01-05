package com.imstargg.core.domain.statistics;

public record BattleEventBrawlerResultStatistics(
        long brawlerBrawlStarsId,
        long victoryCount,
        long defeatCount,
        long drawCount,
        long starPlayerCount
) {
}
