package com.imstargg.core.domain.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class StatisticsParamBuilder {

    private LocalDate date;
    @Nullable
    private TrophyRangeRange trophyRange;
    @Nullable
    private SoloRankTierRangeRange soloRankTierRange;

    public StatisticsParamBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public StatisticsParamBuilder trophyRange(TrophyRangeRange trophyRange) {
        this.trophyRange = trophyRange;
        return this;
    }

    public StatisticsParamBuilder soloRankTierRange(SoloRankTierRangeRange soloRankTierRange) {
        this.soloRankTierRange = soloRankTierRange;
        return this;
    }

    public <T> List<T> build(BuildFunction<T> function) {
        if (trophyRange != null) {
            return lastAWeekStream().flatMap(battleDate ->
                    trophyRange.getRanges().stream().map(tr ->
                            function.build(battleDate, tr, null)
                    )
            ).toList();
        } else if (soloRankTierRange != null) {
            return lastAWeekStream().flatMap(battleDate ->
                    soloRankTierRange.getRanges().stream().map(srtr ->
                            function.build(battleDate, null, srtr)
                    )
            ).toList();
        }

        List<T> result = new ArrayList<>();
        lastAWeekStream()
                .map(battleDate -> function.build(battleDate, null, null))
                .forEach(result::add);
        lastAWeekStream().flatMap(battleDate ->
                Arrays.stream(TrophyRange.values()).map(tr ->
                        function.build(battleDate, tr, null)
                )
        ).forEach(result::add);
        lastAWeekStream().flatMap(battleDate ->
                Arrays.stream(SoloRankTierRange.values()).map(srtr ->
                        function.build(battleDate, null, srtr)
                )
        ).forEach(result::add);
        return Collections.unmodifiableList(result);
    }

    private Stream<LocalDate> lastAWeekStream() {
        return Stream.iterate(date, d -> d.minusDays(1))
                .limit(7);
    }


    @FunctionalInterface
    public interface BuildFunction<T> {

        T build(
                LocalDate battleDate,
                @Nullable TrophyRange trophyRange,
                @Nullable SoloRankTierRange soloRankTierRange
        );
    }
}
