package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlersRankStatisticsParam;
import com.imstargg.core.enums.DateRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlersRankStatisticsRequest(
        @NotNull LocalDate date,
        @NotNull Long brawlerId,
        @NotNull DateRange dateRange,
        @NotNull TrophyRangeRange trophyRange
) {

    public BattleEventBrawlersRankStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlersRankStatisticsParam(
                eventId,
                new BrawlStarsId(brawlerId),
                date(),
                dateRange(),
                trophyRange(),
                2
        );
    }
}
