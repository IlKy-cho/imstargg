package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlerResultStatisticsParams;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BattleEventBrawlerResultStatisticsRequest(
        @Positive long eventId,
        @PastOrPresent LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange SoloRankTierRange,
        boolean duplicateBrawler
) {

    public BattleEventBrawlerResultStatisticsParams toParams() {
        return new BattleEventBrawlerResultStatisticsParams(
                new BrawlStarsId(eventId()),
                date(),
                trophyRange(),
                SoloRankTierRange(),
                duplicateBrawler()
        );
    }
}
