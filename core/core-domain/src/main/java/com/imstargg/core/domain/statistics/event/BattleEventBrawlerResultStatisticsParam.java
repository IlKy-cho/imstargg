package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.StatisticsParamBuilder;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record BattleEventBrawlerResultStatisticsParam(
        BrawlStarsId eventId,
        LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange,
        boolean duplicateBrawler
) {

    public List<BattleEventBrawlerResultCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .trophyRange(trophyRange)
                .soloRankTierRange(soloRankTierRange)
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

}
