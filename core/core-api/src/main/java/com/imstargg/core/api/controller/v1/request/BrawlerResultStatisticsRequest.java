package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.statistics.brawler.BrawlerResultStatisticsParam;
import com.imstargg.core.enums.DateRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BrawlerResultStatisticsRequest(
        @NotNull LocalDate date,
        @NotNull DateRange dateRange,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public BrawlerResultStatisticsParam toParam() {
        return new BrawlerResultStatisticsParam(
                date(),
                dateRange(),
                trophyRange(),
                soloRankTierRange()
        );
    }
}
