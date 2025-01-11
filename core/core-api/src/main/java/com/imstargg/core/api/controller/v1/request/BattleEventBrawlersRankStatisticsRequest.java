package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlersRankStatisticsParams;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record BattleEventBrawlersRankStatisticsRequest(
        @PastOrPresent LocalDate date,
        @NotNull TrophyRangeRange trophyRange
) {

    public BattleEventBrawlersRankStatisticsParams toParams(BrawlStarsId eventId) {
        return new BattleEventBrawlersRankStatisticsParams(
                eventId,
                date(),
                trophyRange(),
                2
        );
    }
}
