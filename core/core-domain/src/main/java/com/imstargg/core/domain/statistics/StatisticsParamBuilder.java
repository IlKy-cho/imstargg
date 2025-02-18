package com.imstargg.core.domain.statistics;

import com.imstargg.core.enums.DateRange;
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

public class StatisticsParamBuilder {

    private LocalDate date;
    private DateRange dateRange;
    @Nullable
    private TrophyRangeRange trophyRange;
    @Nullable
    private SoloRankTierRangeRange soloRankTierRange;

    public StatisticsParamBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public StatisticsParamBuilder dateRange(DateRange dateRange) {
        this.dateRange = dateRange;
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
            return dateRange.lastDates(date).stream().flatMap(battleDate ->
                    trophyRange.getRanges().stream().map(tr ->
                            function.build(battleDate, tr, null)
                    )
            ).toList();
        } else if (soloRankTierRange != null) {
            return dateRange.lastDates(date).stream().flatMap(battleDate ->
                    soloRankTierRange.getRanges().stream().map(srtr ->
                            function.build(battleDate, null, srtr)
                    )
            ).toList();
        }

        List<T> result = new ArrayList<>();
        dateRange.lastDates(date).stream()
                .map(battleDate -> function.build(battleDate, null, null))
                .forEach(result::add);
        dateRange.lastDates(date).stream().flatMap(battleDate ->
                Arrays.stream(TrophyRange.values()).map(tr ->
                        function.build(battleDate, tr, null)
                )
        ).forEach(result::add);
        dateRange.lastDates(date).stream().flatMap(battleDate ->
                Arrays.stream(SoloRankTierRange.values()).map(srtr ->
                        function.build(battleDate, null, srtr)
                )
        ).forEach(result::add);
        return Collections.unmodifiableList(result);
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
