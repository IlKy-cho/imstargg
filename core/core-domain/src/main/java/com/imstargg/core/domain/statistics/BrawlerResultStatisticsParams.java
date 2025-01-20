package com.imstargg.core.domain.statistics;

import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record BrawlerResultStatisticsParams(
        LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public List<BrawlerResultStatisticsParam> toParamList() {
        return new StatisticsParamBuilder()
                .date(date)
                .trophyRange(trophyRange)
                .soloRankTierRange(soloRankTierRange)
                .build((battleDate, trophyRange, soloRankTierRange, duplicateBrawler) ->
                        new BrawlerResultStatisticsParam(
                                battleDate,
                                trophyRange,
                                soloRankTierRange
                        )
                );
    }

}
