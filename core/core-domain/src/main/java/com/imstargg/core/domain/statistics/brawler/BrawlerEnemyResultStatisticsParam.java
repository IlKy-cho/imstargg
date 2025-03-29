package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.TierRange;

import java.time.LocalDate;

public record BrawlerEnemyResultStatisticsParam(
        BrawlStarsId brawlerId,
        TierRange tierRange,
        LocalDate startDate,
        LocalDate endDate
) {

}
