package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerPairResultStatisticsParam;
import com.imstargg.core.enums.DateRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlersResultStatisticsRequest(
        @NotNull LocalDate date,
        @NotNull Long brawlerId,
        @NotNull DateRange dateRange,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public BattleEventBrawlerPairResultStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlerPairResultStatisticsParam(
                eventId,
                new BrawlStarsId(brawlerId),
                date(),
                dateRange(),
                trophyRange(),
                soloRankTierRange(),
                2
        );
    }
}
