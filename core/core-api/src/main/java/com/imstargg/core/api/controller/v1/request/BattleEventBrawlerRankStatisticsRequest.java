package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.statistics.BattleEventBrawlerRankStatisticsParam;
import com.imstargg.core.enums.TrophyRange;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BattleEventBrawlerRankStatisticsRequest(
        @Positive long eventBrawlStarsId,
        @PastOrPresent LocalDate battleDate,
        @NotNull TrophyRange trophyRange
) {

    public BattleEventBrawlerRankStatisticsParam toParam() {
        return new BattleEventBrawlerRankStatisticsParam(
                eventBrawlStarsId(),
                battleDate(),
                trophyRange()
        );
    }
}
