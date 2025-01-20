package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.utils.DateUtils;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

public record BrawlerBattleEventResultStatisticsParams(
        BrawlStarsId brawlerId,
        LocalDate date,
        @Nullable TrophyRangeRange trophyRange,
        @Nullable SoloRankTierRangeRange soloRankTierRange
) {

    public List<BrawlerBattleEventResultStatisticsParam> toParamList() {
        if (trophyRange != null) {
            return DateUtils.lastAWeekStream(date).flatMap(battleDate ->
                    trophyRange.getRanges().stream().map(trophyRange ->
                            mapParam(trophyRange, null)
                    )
            ).toList();
        } else if (soloRankTierRange != null) {
            return DateUtils.lastAWeekStream(date).flatMap(battleDate ->
                    soloRankTierRange.getRanges().stream().map(soloRankTierRange ->
                            mapParam(null, soloRankTierRange)
                    )
            ).toList();
        }

        return List.of();
    }

    private BrawlerBattleEventResultStatisticsParam mapParam(
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange
    ) {
        return new BrawlerBattleEventResultStatisticsParam(
                brawlerId,
                date,
                trophyRange,
                soloRankTierRange
        );
    }
}
