package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record BrawlerBrawlersResultStatisticsParam(
        BrawlStarsId brawlerId,
        LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange,
        int brawlersNum
) {

    public List<BrawlerBrawlersResultCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .trophyRange(trophyRange)
                .soloRankTierRange(soloRankTierRange)
                .build((battleDate, trophyRange, soloRankTierRange, duplicateBrawler) ->
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
