package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlerRankStatisticsParams;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record BattleEventBrawlerRankStatisticsRequest(
        @PastOrPresent LocalDate date,
        @NotNull TrophyRangeRange trophyRange
) {

    public BattleEventBrawlerRankStatisticsParams toParams(BrawlStarsId eventId) {
        return new BattleEventBrawlerRankStatisticsParams(
                eventId,
                date(),
                trophyRange()
        );
    }
}
