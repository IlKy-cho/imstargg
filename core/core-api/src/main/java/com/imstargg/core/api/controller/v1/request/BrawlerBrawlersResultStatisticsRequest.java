package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BrawlerBrawlersResultStatisticsParams;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BrawlerBrawlersResultStatisticsRequest(
        @NotNull LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public BrawlerBrawlersResultStatisticsParams toParams(BrawlStarsId brawlerId) {
        return new BrawlerBrawlersResultStatisticsParams(
                brawlerId,
                date(),
                trophyRange(),
                soloRankTierRange()
        );
    }
}
