package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.statistics.TierRange;

import java.time.LocalDate;

public record BrawlerResultStatisticsParam(
        TierRange tierRange,
        LocalDate startDate,
        LocalDate endDate
) {
}
