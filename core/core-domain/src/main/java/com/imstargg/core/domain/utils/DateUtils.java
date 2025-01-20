package com.imstargg.core.domain.utils;

import java.time.LocalDate;
import java.util.stream.Stream;

public abstract class DateUtils {

    public static Stream<LocalDate> lastAWeekStream(LocalDate date) {
        return Stream.iterate(date, d -> d.minusDays(1))
                .limit(7);
    }

    private DateUtils() {
        throw new UnsupportedOperationException();
    }
}
