package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.statistics.brawler.BrawlerResultStatisticsParam;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BrawlerResultStatisticsRequest(
        @NotNull LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public BrawlerResultStatisticsParam toParam() {
        return new BrawlerResultStatisticsParam(
                date(),
                trophyRange(),
                soloRankTierRange()
        );
    }
}
