package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.utils.DateUtils;
import com.imstargg.core.enums.TrophyRangeRange;

import java.time.LocalDate;
import java.util.List;

public record BattleEventBrawlersRankStatisticsParams(
        BrawlStarsId eventId,
        LocalDate date,
        TrophyRangeRange trophyRangeRange,
        int brawlersNum
) {

    public List<BattleEventBrawlersRankStatisticsParam> toParamList() {
        return DateUtils.lastAWeekStream(date).flatMap(battleDate ->
                trophyRangeRange.getRanges().stream().map(trophyRange ->
                        new BattleEventBrawlersRankStatisticsParam(
                                eventId,
                                battleDate,
                                trophyRange,
                                brawlersNum
                        )
                )
        ).toList();
    }

}
