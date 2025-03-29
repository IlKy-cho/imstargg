package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.TrophyRange;

import java.time.LocalDate;

public record BattleEventBrawlerPairRankStatisticsParam(
        BrawlStarsId eventId,
        BrawlStarsId brawlerId,
        TrophyRange trophyRange,
        LocalDate startDate,
        LocalDate endDate
) {


}
