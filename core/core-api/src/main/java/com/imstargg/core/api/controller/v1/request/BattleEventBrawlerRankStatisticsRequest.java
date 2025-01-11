package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlerRankStatisticsParams;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BattleEventBrawlerRankStatisticsRequest(
        @Positive long eventBrawlStarsId,
        @PastOrPresent LocalDate battleDate,
        @NotNull TrophyRangeRange trophyRange
) {

    public BattleEventBrawlerRankStatisticsParams toParams() {
        return new BattleEventBrawlerRankStatisticsParams(
                new BrawlStarsId(eventBrawlStarsId()),
                battleDate(),
                trophyRange()
        );
    }
}
