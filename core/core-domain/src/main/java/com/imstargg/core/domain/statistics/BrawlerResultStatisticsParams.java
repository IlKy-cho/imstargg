package com.imstargg.core.domain.statistics;

import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public record BrawlerResultStatisticsParams(
        LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public List<BrawlerResultStatisticsParam> toParamList() {
        if (trophyRange != null) {
            return battleDateWeekStream().flatMap(battleDate ->
                    trophyRange.getRanges().stream().map(trophyRange ->
                            new BrawlerResultStatisticsParam(
                                    battleDate,
                                    trophyRange,
                                    null
                            )
                    )
            ).toList();
        } else if (soloRankTierRange != null) {
            return battleDateWeekStream().flatMap(date ->
                    soloRankTierRange.getRanges().stream().map(soloRankTierRange ->
                            new BrawlerResultStatisticsParam(
                                    date,
                                    null,
                                    soloRankTierRange
                            )
                    )
            ).toList();
        }

        return List.of();
    }

    private Stream<LocalDate> battleDateWeekStream() {
        return Stream.iterate(date, date -> date.minusDays(1))
                .limit(7);
    }
}
