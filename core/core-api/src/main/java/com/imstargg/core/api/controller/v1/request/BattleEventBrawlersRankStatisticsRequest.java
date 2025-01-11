package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlersRankStatisticsParams;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BattleEventBrawlersRankStatisticsRequest(
        @Positive long eventId,
        @PastOrPresent LocalDate date,
        @NotNull TrophyRangeRange trophyRange
) {

    public BattleEventBrawlersRankStatisticsParams toParams() {
        return new BattleEventBrawlersRankStatisticsParams(
                new BrawlStarsId(eventId()),
                date(),
                trophyRange(),
                2
        );
    }
}
