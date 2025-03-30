package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerPairRankStatisticsParam;
import com.imstargg.core.enums.TrophyRange;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlerPairRankStatisticsRequest(
        @NotNull Long brawlerId,
        @NotNull TrophyRange trophyRange,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {

    public BattleEventBrawlerPairRankStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlerPairRankStatisticsParam(
                eventId,
                new BrawlStarsId(brawlerId),
                trophyRange,
                startDate,
                endDate
        );
    }
}
