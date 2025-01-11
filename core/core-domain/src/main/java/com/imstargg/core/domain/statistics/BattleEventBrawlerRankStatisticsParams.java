package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.TrophyRangeRange;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public record BattleEventBrawlerRankStatisticsParams(
        BrawlStarsId eventId,
        LocalDate date,
        TrophyRangeRange trophyRangeRange
) {

    public List<BattleEventBrawlerRankStatisticsParam> toParamList() {
        return battleDateWeekStream().flatMap(battleDate ->
                trophyRangeRange.getRanges().stream().map(trophyRange ->
                        new BattleEventBrawlerRankStatisticsParam(
                                eventId,
                                battleDate,
                                trophyRange
                        )
                )
        ).toList();
    }

    private Stream<LocalDate> battleDateWeekStream() {
        return Stream.iterate(date, battleDate -> battleDate.minusDays(1))
                .limit(7);
    }
}
