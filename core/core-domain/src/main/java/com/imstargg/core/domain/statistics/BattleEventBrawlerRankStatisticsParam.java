package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.TrophyRange;

import java.time.LocalDate;

public record BattleEventBrawlerRankStatisticsParam(
        BrawlStarsId eventId,
        LocalDate battleDate,
        TrophyRange trophyRange
) {
}
