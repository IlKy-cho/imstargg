package com.imstargg.batch.domain.statistics;

import java.time.LocalDate;

public interface StatisticsCollectorFactory<T> {
    
    StatisticsCollector<T> create(LocalDate battleDate);
}
