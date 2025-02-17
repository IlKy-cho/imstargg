package com.imstargg.core.enums;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public enum DateRange {

    ONE_DAY(1),
    THREE_DAYS(3),
    ONE_WEEK(7),
    TWO_WEEKS(14),
    ;

    private final int value;

    DateRange(int value) {
        this.value = value;
    }

    List<LocalDate> lastDates(LocalDate date) {
        return Stream.iterate(date, d -> d.minusDays(1))
                .limit(value)
                .toList();
    }
}
