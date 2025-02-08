package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlersRankStatisticsParam;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlersRankStatisticsRequest(
        @NotNull LocalDate date,
        @NotNull TrophyRangeRange trophyRange
) {

    public BattleEventBrawlersRankStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlersRankStatisticsParam(
                eventId,
                date(),
                trophyRange(),
                2
        );
    }
}
