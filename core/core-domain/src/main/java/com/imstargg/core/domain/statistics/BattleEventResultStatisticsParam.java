package com.imstargg.core.domain.statistics;

import com.imstargg.core.enums.SoloRankTier;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record BattleEventResultStatisticsParam(
        long eventBrawlStarsId,
        LocalDate battleDate,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTier soloRankTier,
        boolean duplicateBrawler
) {
}
