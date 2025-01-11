package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventBrawlersResultStatisticsParams;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record BattleEventBrawlersResultStatisticsRequest(
        @PastOrPresent LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange,
        boolean duplicateBrawler
) {

    public BattleEventBrawlersResultStatisticsParams toParams(BrawlStarsId eventId) {
        return new BattleEventBrawlersResultStatisticsParams(
                eventId,
                date(),
                trophyRange(),
                soloRankTierRange(),
                2,
                duplicateBrawler()
        );
    }
}
