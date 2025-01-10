package com.imstargg.core.domain.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record BattleEventBrawlersResultStatisticsParam(
        long eventBrawlStarsId,
        LocalDate battleDate,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        int brawlersNum,
        boolean duplicateBrawler
) {
}
