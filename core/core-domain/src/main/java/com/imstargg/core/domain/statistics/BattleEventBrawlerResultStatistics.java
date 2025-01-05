package com.imstargg.core.domain.statistics;

public record BattleEventBrawlerResultStatistics(
        long brawlerBrawlStarsId,
        int victoryCount,
        int defeatCount,
        int drawCount,
        int starPlayerCount
) {
}
