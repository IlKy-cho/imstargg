package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.brawler.BrawlerBattleEventResultStatisticsParam;
import com.imstargg.core.enums.DateRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BrawlerBattleEventResultStatisticsRequest(
        @NotNull LocalDate date,
        @NotNull DateRange dateRange,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public BrawlerBattleEventResultStatisticsParam toParam(BrawlStarsId brawlerId) {
        return new BrawlerBattleEventResultStatisticsParam(
                brawlerId,
                date(),
                dateRange(),
                trophyRange(),
                soloRankTierRange()
        );
    }
}
