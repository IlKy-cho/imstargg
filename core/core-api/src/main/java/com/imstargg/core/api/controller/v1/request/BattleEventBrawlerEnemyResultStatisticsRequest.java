package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerEnemyResultStatisticsParam;
import com.imstargg.core.enums.DateRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlerEnemyResultStatisticsRequest(
        @NotNull LocalDate date,
        @NotNull Long brawlerId,
        @NotNull DateRange dateRange,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public BattleEventBrawlerEnemyResultStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlerEnemyResultStatisticsParam(
                eventId,
                new BrawlStarsId(brawlerId),
                date(),
                dateRange(),
                trophyRange(),
                soloRankTierRange()
        );
    }
}
