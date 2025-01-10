package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public record BattleEventBrawlerResultStatisticsParams(
        BrawlStarsId eventId,
        LocalDate battleDate,
        @Nullable TrophyRangeRange trophyRangeRange,
        @Nullable SoloRankTierRangeRange soloRankTierRangeRange,
        boolean duplicateBrawler
) {

    public List<BattleEventBrawlerResultStatisticsParam> toParamList() {
        if (trophyRangeRange != null) {
            return battleDateWeekStream().flatMap(date ->
                    trophyRangeRange.getRanges().stream().flatMap(trophyRange ->
                            mapParam(date, trophyRange, null, duplicateBrawler)
                    )
            ).toList();
        } else if (soloRankTierRangeRange != null) {
            return battleDateWeekStream().flatMap(date ->
                    soloRankTierRangeRange.getRanges().stream().flatMap(soloRankTierRange ->
                            mapParam(date, null, soloRankTierRange, duplicateBrawler)
                    )
            ).toList();
        }

        return List.of();
    }

    private Stream<LocalDate> battleDateWeekStream() {
        return Stream.iterate(battleDate, date -> date.minusDays(1))
                .limit(7);
    }

    private Stream<BattleEventBrawlerResultStatisticsParam> mapParam(
            LocalDate date,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange,
            boolean duplicateBrawler
    ) {
        return !duplicateBrawler
                ? Stream.of(
                new BattleEventBrawlerResultStatisticsParam(
                        eventId,
                        date,
                        trophyRange,
                        soloRankTierRange,
                        false
                ))
                :
                Stream.of(
                        new BattleEventBrawlerResultStatisticsParam(
                                eventId,
                                date,
                                trophyRange,
                                soloRankTierRange,
                                false
                        ),
                        new BattleEventBrawlerResultStatisticsParam(
                                eventId,
                                date,
                                trophyRange,
                                soloRankTierRange,
                                true
                        )
                );
    }
}
