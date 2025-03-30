package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.TierRange;
import com.imstargg.core.domain.statistics.event.BattleEventBrawlerEnemyResultStatisticsParam;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlerEnemyResultStatisticsRequest(
        @NotNull Long brawlerId,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {

    public BattleEventBrawlerEnemyResultStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlerEnemyResultStatisticsParam(
                eventId,
                new BrawlStarsId(brawlerId),
                new TierRange(trophyRange, soloRankTierRange),
                startDate,
                endDate
        );
    }
}
