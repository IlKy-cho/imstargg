package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record BattleEventBrawlerResultCountParam(
        BrawlStarsId eventId,
        LocalDate battleDate,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange
) {
}
