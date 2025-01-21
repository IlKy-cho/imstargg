package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.TrophyRange;

import java.time.LocalDate;

public record BattleEventBrawlersRankCountParam(
        BrawlStarsId eventId,
        LocalDate battleDate,
        TrophyRange trophyRange,
        int brawlersNum
) {

}
