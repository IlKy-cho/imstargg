package com.imstargg.core.domain.statistics;

import com.imstargg.core.enums.TrophyRange;

import java.time.LocalDate;

public record BattleEventBrawlerRankStatisticsParam(
        long eventBrawlStarsId,
        LocalDate battleDate,
        TrophyRange trophyRange
) {
}
