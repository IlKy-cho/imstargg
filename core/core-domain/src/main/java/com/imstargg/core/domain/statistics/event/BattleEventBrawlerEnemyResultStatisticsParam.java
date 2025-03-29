package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record BattleEventBrawlerEnemyResultStatisticsParam(
        BrawlStarsId eventId,
        BrawlStarsId brawlerId,
        LocalDate startDate,
        LocalDate endDate,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange
) {
}
