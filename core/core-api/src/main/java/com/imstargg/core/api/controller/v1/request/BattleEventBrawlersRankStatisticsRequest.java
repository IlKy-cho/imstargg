package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.statistics.BattleEventBrawlersRankStatisticsParam;
import com.imstargg.core.enums.TrophyRange;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

public record BattleEventBrawlersRankStatisticsRequest(
        @Positive long eventBrawlStarsId,
        @PastOrPresent LocalDate battleDate,
        @NotNull TrophyRange trophyRange,
        @Range(min = 2, max = 5) int brawlersNum
) {

    public BattleEventBrawlersRankStatisticsParam toParam() {
        return new BattleEventBrawlersRankStatisticsParam(
                eventBrawlStarsId(),
                battleDate(),
                trophyRange(),
                brawlersNum()
        );
    }
}
