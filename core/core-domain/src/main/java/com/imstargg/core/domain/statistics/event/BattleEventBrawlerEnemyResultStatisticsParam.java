package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.TierRange;

import java.time.LocalDate;

public record BattleEventBrawlerEnemyResultStatisticsParam(
        BrawlStarsId eventId,
        BrawlStarsId brawlerId,
        LocalDate startDate,
        LocalDate endDate,
        TierRange tierRange
) {
}
