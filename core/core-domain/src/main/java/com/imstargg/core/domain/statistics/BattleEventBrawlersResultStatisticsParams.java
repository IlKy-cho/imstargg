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

public record BattleEventBrawlersResultStatisticsParams(
        BrawlStarsId eventId,
        LocalDate battleDate,
        @Nullable TrophyRangeRange trophyRangeRange,
        @Nullable SoloRankTierRangeRange soloRankTierRangeRange,
        int brawlersNum,
        boolean duplicateBrawler
) {

    public List<BattleEventBrawlersResultStatisticsParam> toParamList() {
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

    private Stream<BattleEventBrawlersResultStatisticsParam> mapParam(
            LocalDate date,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange,
            boolean duplicateBrawler
    ) {
        return !duplicateBrawler
                ? Stream.of(
                new BattleEventBrawlersResultStatisticsParam(
                        eventId,
                        date,
                        trophyRange,
                        soloRankTierRange,
                        brawlersNum,
                        false
                ))
                :
                Stream.of(
                        new BattleEventBrawlersResultStatisticsParam(
                                eventId,
                                date,
                                trophyRange,
                                soloRankTierRange,
                                brawlersNum,
                                false
                        ),
                        new BattleEventBrawlersResultStatisticsParam(
                                eventId,
                                date,
                                trophyRange,
                                soloRankTierRange,
                                brawlersNum,
                                true
                        )
                );
    }
}
