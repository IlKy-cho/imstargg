package com.imstargg.core.domain.statistics;

import java.time.LocalDate;

public record BattleEventBrawlerResultStatistics(
        long brawlerBrawlStarsId,
        LocalDate battleDate,
        int victoryCount,
        int defeatCount,
        int drawCount,
        int starPlayerCount
) {
}
