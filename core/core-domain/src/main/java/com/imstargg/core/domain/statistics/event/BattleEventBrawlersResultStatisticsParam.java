package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.StatisticsParamBuilder;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record BattleEventBrawlersResultStatisticsParam(
        BrawlStarsId eventId,
        LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange,
        int brawlersNum,
        boolean duplicateBrawler
) {

    public List<BattleEventBrawlersResultCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .trophyRange(trophyRange)
                .soloRankTierRange(soloRankTierRange)
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
