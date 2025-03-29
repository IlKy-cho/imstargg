package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.DateRange;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record BrawlerBattleEventResultStatisticsParam(
        BrawlStarsId brawlerId,
        LocalDate date,
        DateRange dateRange,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange
) {

    public List<BrawlerBattleEventResultCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .dateRange(dateRange)
                .trophyRange(trophyRange)
                .soloRankTierRange(soloRankTierRange)
                .build((battleDate, trophyRange, soloRankTierRange) ->
                        new BrawlerBattleEventResultCountParam(
                                brawlerId,
                                battleDate,
                                trophyRange,
                                soloRankTierRange
                        )
                );
    }
}
