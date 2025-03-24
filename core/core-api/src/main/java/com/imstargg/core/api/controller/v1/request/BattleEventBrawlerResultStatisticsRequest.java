package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerResultStatisticsParam;
import com.imstargg.core.enums.DateRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlerResultStatisticsRequest(
        @NotNull LocalDate date,
        @NotNull DateRange dateRange,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public BattleEventBrawlerResultStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlerResultStatisticsParam(
                eventId,
                date(),
                dateRange(),
                trophyRange(),
                soloRankTierRange()
        );
    }
}
