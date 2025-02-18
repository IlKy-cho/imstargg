package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.StatisticsParamBuilder;
import com.imstargg.core.enums.DateRange;
import com.imstargg.core.enums.TrophyRangeRange;

import java.time.LocalDate;
import java.util.List;

public record BattleEventBrawlersRankStatisticsParam(
        BrawlStarsId eventId,
        BrawlStarsId brawlerId,
        LocalDate date,
        DateRange dateRange,
        TrophyRangeRange trophyRangeRange,
        int brawlersNum
) {

    public List<BattleEventBrawlersRankCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .dateRange(dateRange)
                .trophyRange(trophyRangeRange)
                .build((battleDate, trophyRange, soloRankTierRange) ->
                        new BattleEventBrawlersRankCountParam(
                                eventId,
                                brawlerId,
                                battleDate,
                                trophyRange,
                                brawlersNum
                        )
                );
    }

}
