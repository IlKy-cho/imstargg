package com.imstargg.batch.job.statistics;

import org.springframework.batch.item.support.ListItemReader;

import java.time.Clock;
import java.time.LocalDate;

public abstract class StatisticsJobItemReaderFactoryUtils {

    public static ListItemReader<LocalDate> create(Clock clock) {
        LocalDate today = LocalDate.now(clock);
        return new ListItemReader<>(today.minusDays(30).datesUntil(today).toList());
    }

    private StatisticsJobItemReaderFactoryUtils() {
        throw new UnsupportedOperationException();
    }
}
