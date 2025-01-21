package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record BattleEventBrawlerResultStatisticsParam(
        BrawlStarsId eventId,
        LocalDate date,
        @Nullable TrophyRangeRange trophyRangeRange,
        @Nullable SoloRankTierRangeRange soloRankTierRangeRange,
        boolean duplicateBrawler
) {

    public List<BattleEventBrawlerResultCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .trophyRange(trophyRangeRange)
                .soloRankTierRange(soloRankTierRangeRange)
                .duplicateBrawler(duplicateBrawler)
                .build((battleDate, trophyRange, soloRankTierRange, duplicateBrawler) ->
                        new BattleEventBrawlerResultCountParam(
                                eventId,
                                battleDate,
                                trophyRange,
                                soloRankTierRange,
                                Boolean.TRUE.equals(duplicateBrawler)
                        )
                );
    }

    private Stream<BattleEventBrawlerResultCountParam> mapParam(
            LocalDate battleDate,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange,
            boolean duplicateBrawler
    ) {
        List<BattleEventBrawlerResultCountParam> result = new ArrayList<>();
        result.add(new BattleEventBrawlerResultCountParam(
                eventId,
                battleDate,
                trophyRange,
                soloRankTierRange,
                false
        ));
        if (duplicateBrawler) {
            result.add(new BattleEventBrawlerResultCountParam(
                    eventId,
                    battleDate,
                    trophyRange,
                    soloRankTierRange,
                    true
            ));
        }
        return result.stream();
    }
}
