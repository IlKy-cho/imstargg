package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.StatisticsCheckPointCollectionJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class StatisticsCheckPointerFactory {

    private final StatisticsCheckPointCollectionJpaRepository repository;

    public StatisticsCheckPointerFactory(StatisticsCheckPointCollectionJpaRepository repository) {
        this.repository = repository;
    }

    public StatisticsCheckPointer create(String key) {
        return new StatisticsCheckPointer(key, repository);
    }
}
