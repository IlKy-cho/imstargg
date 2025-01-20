package com.imstargg.core.domain.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.SoloRankTierRangeRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.core.enums.TrophyRangeRange;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class StatisticsParamBuilder {

    private LocalDate date;
    @Nullable
    private TrophyRangeRange trophyRange;
    @Nullable
    private SoloRankTierRangeRange soloRankTierRange;
    @Nullable
    private Boolean duplicateBrawler;

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

    public StatisticsParamBuilder duplicateBrawler(Boolean duplicateBrawler) {
        this.duplicateBrawler = duplicateBrawler;
        return this;
    }

    public <T> List<T> build(BuildFunction<T> function) {
        if (trophyRange != null) {
            return lastAWeekStream().flatMap(battleDate -> duplicateBrawlerStream().flatMap(db ->
                    trophyRange.getRanges().stream().map(tr ->
                            function.build(battleDate, tr, null, db)
                    )
            )).toList();
        } else if (soloRankTierRange != null) {
            return lastAWeekStream().flatMap(battleDate -> duplicateBrawlerStream().flatMap(db ->
                    soloRankTierRange.getRanges().stream().map(srtr ->
                            function.build(battleDate, null, srtr, db)
                    )
            )).toList();
        }

        return List.of();
    }

    private Stream<LocalDate> lastAWeekStream() {
        return Stream.iterate(date, d -> d.minusDays(1))
                .limit(7);
    }

    private Stream<Boolean> duplicateBrawlerStream() {
        if (duplicateBrawler == null || !duplicateBrawler) {
            return Stream.of(false);
        } else {
            return Stream.of(false, true);
        }
    }

    @FunctionalInterface
    public interface BuildFunction<T> {

        T build(
                LocalDate battleDate,
                @Nullable TrophyRange trophyRange,
                @Nullable SoloRankTierRange soloRankTierRange,
                @Nullable Boolean duplicateBrawler
        );
    }
}
