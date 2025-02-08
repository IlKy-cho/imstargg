package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.StatisticsParamBuilder;
import com.imstargg.core.enums.TrophyRangeRange;

import java.time.LocalDate;
import java.util.List;

public record BattleEventBrawlerRankStatisticsParam(
        BrawlStarsId eventId,
        LocalDate date,
        TrophyRangeRange trophyRangeRange
) {

    public List<BattleEventBrawlerRankCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .trophyRange(trophyRangeRange)
                .build((battleDate, trophyRange, soloRankTierRange, duplicateBrawler) ->
                        new BattleEventBrawlerRankCountParam(
                                eventId,
                                battleDate,
                                trophyRange
                        )
                );
    }
}
