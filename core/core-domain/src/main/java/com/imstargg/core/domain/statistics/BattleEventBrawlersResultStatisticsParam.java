package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record BattleEventBrawlersResultStatisticsParam(
        BrawlStarsId eventId,
        LocalDate date,
        @Nullable TrophyRangeRange trophyRangeRange,
        @Nullable SoloRankTierRangeRange soloRankTierRangeRange,
        int brawlersNum,
        boolean duplicateBrawler
) {

    public List<BattleEventBrawlersResultCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .trophyRange(trophyRangeRange)
                .soloRankTierRange(soloRankTierRangeRange)
                .duplicateBrawler(duplicateBrawler)
                .build((battleDate, trophyRange, soloRankTierRange, duplicateBrawler) ->
                        new BattleEventBrawlersResultCountParam(
                                eventId,
                                battleDate,
                                trophyRange,
                                soloRankTierRange,
                                brawlersNum,
                                Boolean.TRUE.equals(duplicateBrawler)
                        )
                );
    }
}
