package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.statistics.TierRange;
import com.imstargg.core.domain.statistics.brawler.BrawlerResultStatisticsParam;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BrawlerResultStatisticsRequest(
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {

    public BrawlerResultStatisticsParam toParam() {
        return new BrawlerResultStatisticsParam(
                new TierRange(trophyRange, soloRankTierRange),
                startDate,
                endDate
        );
    }
}
