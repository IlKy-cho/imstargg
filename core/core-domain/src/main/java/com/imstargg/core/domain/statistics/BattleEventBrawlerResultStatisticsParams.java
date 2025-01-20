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

public record BattleEventBrawlerResultStatisticsParams(
        BrawlStarsId eventId,
        LocalDate date,
        @Nullable TrophyRangeRange trophyRangeRange,
        @Nullable SoloRankTierRangeRange soloRankTierRangeRange,
        boolean duplicateBrawler
) {

    public List<BattleEventBrawlerResultStatisticsParam> toParamList() {
        return new StatisticsParamBuilder()
                .date(date)
                .trophyRange(trophyRangeRange)
                .soloRankTierRange(soloRankTierRangeRange)
                .duplicateBrawler(duplicateBrawler)
                .build((battleDate, trophyRange, soloRankTierRange, duplicateBrawler) ->
                        new BattleEventBrawlerResultStatisticsParam(
                                eventId,
                                battleDate,
                                trophyRange,
                                soloRankTierRange,
                                Boolean.TRUE.equals(duplicateBrawler)
                        )
                );
    }

    private Stream<BattleEventBrawlerResultStatisticsParam> mapParam(
            LocalDate battleDate,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange,
            boolean duplicateBrawler
    ) {
        List<BattleEventBrawlerResultStatisticsParam> result = new ArrayList<>();
        result.add(new BattleEventBrawlerResultStatisticsParam(
                eventId,
                battleDate,
                trophyRange,
                soloRankTierRange,
                false
        ));
        if (duplicateBrawler) {
            result.add(new BattleEventBrawlerResultStatisticsParam(
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
