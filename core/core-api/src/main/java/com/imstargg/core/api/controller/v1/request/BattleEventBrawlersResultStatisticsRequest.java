package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlersResultStatisticsParams;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BattleEventBrawlersResultStatisticsRequest(
        @Positive long eventId,
        @PastOrPresent LocalDate battleDate,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange,
        boolean duplicateBrawler
) {

    public BattleEventBrawlersResultStatisticsParams toParams() {
        return new BattleEventBrawlersResultStatisticsParams(
                new BrawlStarsId(eventId()),
                battleDate(),
                trophyRange(),
                soloRankTierRange(),
                2,
                duplicateBrawler()
        );
    }
}
