package com.imstargg.batch.domain.statistics;

public interface StatisticsCollectorFactory<T> {

    StatisticsCollector<T> create(long eventBrawlStarsId);
}
