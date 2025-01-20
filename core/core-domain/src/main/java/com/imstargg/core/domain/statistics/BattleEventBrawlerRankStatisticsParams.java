package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.utils.DateUtils;
import com.imstargg.core.enums.TrophyRangeRange;

import java.time.LocalDate;
import java.util.List;

public record BattleEventBrawlerRankStatisticsParams(
        BrawlStarsId eventId,
        LocalDate date,
        TrophyRangeRange trophyRangeRange
) {

    public List<BattleEventBrawlerRankStatisticsParam> toParamList() {
        return DateUtils.lastAWeekStream(date).flatMap(battleDate ->
                trophyRangeRange.getRanges().stream().map(trophyRange ->
                        new BattleEventBrawlerRankStatisticsParam(
                                eventId,
                                battleDate,
                                trophyRange
                        )
                )
        ).toList();
    }
}
