package com.imstargg.core.domain.statistics;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.utils.DateUtils;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record BattleEventBrawlerResultStatisticsParams(
        BrawlStarsId eventId,
        LocalDate date,
        @Nullable TrophyRangeRange trophyRangeRange,
        @Nullable SoloRankTierRangeRange soloRankTierRangeRange,
        boolean duplicateBrawler
) {

    public List<BattleEventBrawlerResultStatisticsParam> toParamList() {
        if (trophyRangeRange != null) {
            return DateUtils.lastAWeekStream(date).flatMap(battleDate ->
                    trophyRangeRange.getRanges().stream().flatMap(trophyRange ->
                            mapParam(battleDate, trophyRange, null, duplicateBrawler)
                    )
            ).toList();
        } else if (soloRankTierRangeRange != null) {
            return DateUtils.lastAWeekStream(date).flatMap(date ->
                    soloRankTierRangeRange.getRanges().stream().flatMap(soloRankTierRange ->
                            mapParam(date, null, soloRankTierRange, duplicateBrawler)
                    )
            ).toList();
        }

        return List.of();
    }

    private Stream<BattleEventBrawlerResultStatisticsParam> mapParam(
            LocalDate battleDate,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange,
            boolean duplicateBrawler
    ) {
        List<BattleEventBrawlerResultStatisticsParam> result = new ArrayList<>();
        result.add(new BattleEventBrawlerResultStatisticsParam(
                eventId,
                battleDate,
                trophyRange,
                soloRankTierRange,
                false
        ));
        if (duplicateBrawler) {
            result.add(new BattleEventBrawlerResultStatisticsParam(
                    eventId,
                    battleDate,
                    trophyRange,
                    soloRankTierRange,
                    true
            ));
        }
        return result.stream();
    }
}
