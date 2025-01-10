package com.imstargg.core.api.controller.v1.request;

import com.imstargg.core.domain.statistics.BattleEventBrawlersResultStatisticsParam;
import com.imstargg.core.enums.SoloRankTier;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

public record BattleEventBrawlersResultStatisticsRequest(
        @Positive long eventBrawlStarsId,
        @PastOrPresent LocalDate battleDate,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        @Range(min = 2, max = 5) int brawlersNum,
        boolean duplicateBrawler
) {

    public BattleEventBrawlersResultStatisticsParam toParam() {
        return new BattleEventBrawlersResultStatisticsParam(
                eventBrawlStarsId(),
                battleDate(),
                trophyRange(),
                soloRankTierRange(),
                brawlersNum(),
                duplicateBrawler()
        );
    }
}
