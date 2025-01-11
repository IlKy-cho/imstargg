package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.TrophyRangeRange;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public record BattleEventBrawlersRankStatisticsParams(
        BrawlStarsId eventId,
        LocalDate battleDate,
        TrophyRangeRange trophyRangeRange,
        int brawlersNum
) {

    public List<BattleEventBrawlersRankStatisticsParam> toParamList() {
        return battleDateWeekStream().flatMap(date ->
                trophyRangeRange.getRanges().stream().map(trophyRange ->
                        new BattleEventBrawlersRankStatisticsParam(
                                eventId,
                                date,
                                trophyRange,
                                brawlersNum
                        )
                )
        ).toList();
    }

    private Stream<LocalDate> battleDateWeekStream() {
        return Stream.iterate(battleDate, date -> date.minusDays(1))
                .limit(7);
    }
}
