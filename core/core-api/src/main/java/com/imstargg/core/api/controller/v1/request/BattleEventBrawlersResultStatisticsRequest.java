package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlersResultStatisticsParam;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlersResultStatisticsRequest(
        @NotNull LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange,
        boolean duplicateBrawler
) {

    public BattleEventBrawlersResultStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlersResultStatisticsParam(
                eventId,
                date(),
                trophyRange(),
                soloRankTierRange(),
                2,
                duplicateBrawler()
        );
    }
}
