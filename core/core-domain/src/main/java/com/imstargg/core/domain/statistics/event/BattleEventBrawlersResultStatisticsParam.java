package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.StatisticsParamBuilder;
import com.imstargg.core.enums.DateRange;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record BattleEventBrawlersResultStatisticsParam(
        BrawlStarsId eventId,
        BrawlStarsId brawlerId,
        LocalDate date,
        DateRange dateRange,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange,
        int brawlersNum
) {

    public List<BattleEventBrawlersResultCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .dateRange(dateRange)
                .trophyRange(trophyRange)
                .soloRankTierRange(soloRankTierRange)
                .build((battleDate, trophyRange, soloRankTierRange) ->
                        new BattleEventBrawlersResultCountParam(
                                eventId,
                                brawlerId,
                                battleDate,
                                trophyRange,
                                soloRankTierRange,
                                brawlersNum
                        )
                );
    }
}
