package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.StatisticsParamBuilder;
import com.imstargg.core.enums.DateRange;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record BattleEventBrawlerEnemyResultStatisticsParam(
        BrawlStarsId eventId,
        BrawlStarsId brawlerId,
        LocalDate date,
        DateRange dateRange,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public List<BattleEventBrawlerEnemyResultCountParam> toCountParams() {
        return new StatisticsParamBuilder()
                .date(date)
                .dateRange(dateRange)
                .trophyRange(trophyRange)
                .soloRankTierRange(soloRankTierRange)
                .build((battleDate, trophyRange, soloRankTierRange) ->
                        new BattleEventBrawlerEnemyResultCountParam(
                                eventId,
                                brawlerId,
                                battleDate,
                                trophyRange,
                                soloRankTierRange
                        )
                );
    }
}
