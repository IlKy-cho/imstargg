package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.TierRange;

import java.time.LocalDate;

public record BattleEventBrawlerPairResultStatisticsParam(
        BrawlStarsId eventId,
        BrawlStarsId brawlerId,
        TierRange tierRange,
        LocalDate startDate,
        LocalDate endDate
) {

}
