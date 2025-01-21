package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlerResultStatisticsParam;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BattleEventBrawlerResultStatisticsRequest(
        @NotNull LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange SoloRankTierRange,
        boolean duplicateBrawler
) {

    public BattleEventBrawlerResultStatisticsParam toParam(BrawlStarsId eventId) {
        return new BattleEventBrawlerResultStatisticsParam(
                eventId,
                date(),
                trophyRange(),
                SoloRankTierRange(),
                duplicateBrawler()
        );
    }
}
