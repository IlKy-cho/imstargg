package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.brawler.BrawlerBrawlersResultStatisticsParam;
import com.imstargg.core.enums.DateRange;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BrawlerBrawlersResultStatisticsRequest(
        @NotNull LocalDate date,
        @NotNull DateRange dateRange,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public BrawlerBrawlersResultStatisticsParam toParam(BrawlStarsId brawlerId) {
        return new BrawlerBrawlersResultStatisticsParam(
                brawlerId,
                date(),
                dateRange(),
                trophyRange(),
                soloRankTierRange(),
                2
        );
    }
}
