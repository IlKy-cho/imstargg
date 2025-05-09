package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.TierRange;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerResultStatisticsParam;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlerResultStatisticsRequest(
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {

    public BattleEventBrawlerResultStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlerResultStatisticsParam(
                eventId,
                new TierRange(trophyRange, soloRankTierRange),
                startDate,
                endDate
        );
    }
}
