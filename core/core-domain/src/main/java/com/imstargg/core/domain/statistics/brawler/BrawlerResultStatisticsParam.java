package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.statistics.StatisticsParamBuilder;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record BrawlerResultStatisticsParam(
        LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public List<BrawlerResultCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .trophyRange(trophyRange)
                .soloRankTierRange(soloRankTierRange)
                .build((battleDate, trophyRange, soloRankTierRange, duplicateBrawler) ->
                        new BrawlerResultCountParam(
                                battleDate,
                                trophyRange,
                                soloRankTierRange
                        )
                );
    }

}
