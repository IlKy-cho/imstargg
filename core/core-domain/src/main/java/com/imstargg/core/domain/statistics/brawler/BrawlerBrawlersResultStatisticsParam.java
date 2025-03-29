package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.DateRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record BrawlerBrawlersResultStatisticsParam(
        BrawlStarsId brawlerId,
        LocalDate date,
        DateRange dateRange,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange,
        int brawlersNum
) {

    public List<BrawlerBrawlersResultCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .dateRange(dateRange)
                .trophyRange(trophyRange)
                .soloRankTierRange(soloRankTierRange)
                .build((battleDate, trophyRange, soloRankTierRange) ->
                        new BrawlerBrawlersResultCountParam(
                                brawlerId,
                                battleDate,
                                trophyRange,
                                soloRankTierRange,
                                brawlersNum
                        )
                );
    }
}
