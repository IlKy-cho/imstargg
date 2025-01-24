package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.brawlstars.BattleEvent;

public record BattleEventResultStatistics(
        BattleEvent event,
        long totalBattleCount,
        double winRate,
        double starPlayerRate
) {
}
