package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerRankStatisticsParam;
import com.imstargg.core.enums.DateRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlerRankStatisticsRequest(
        @NotNull LocalDate date,
        @NotNull DateRange dateRange,
        @NotNull TrophyRangeRange trophyRange
) {

    public BattleEventBrawlerRankStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlerRankStatisticsParam(
                eventId,
                date(),
                dateRange(),
                trophyRange()
        );
    }
}
