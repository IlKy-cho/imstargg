package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.statistics.BattleEventBrawlerResultStatisticsParam;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BattleEventBrawlerResultStatisticsRequest(
        @Positive long eventBrawlStarsId,
        @PastOrPresent LocalDate battleDate,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange SoloRankTierRange,
        boolean duplicateBrawler
) {

    public BattleEventBrawlerResultStatisticsParam toParam() {
        return new BattleEventBrawlerResultStatisticsParam(
                eventBrawlStarsId(),
                battleDate(),
                trophyRange(),
                SoloRankTierRange(),
                duplicateBrawler()
        );
    }
}
