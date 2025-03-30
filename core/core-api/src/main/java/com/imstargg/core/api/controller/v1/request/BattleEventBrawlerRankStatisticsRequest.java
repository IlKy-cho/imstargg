package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerRankStatisticsParam;
import com.imstargg.core.enums.TrophyRange;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlerRankStatisticsRequest(
        @NotNull TrophyRange trophyRange,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {

    public BattleEventBrawlerRankStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlerRankStatisticsParam(
                eventId,
                trophyRange,
                startDate,
                endDate
        );
    }
}
