package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.StatisticsCheckPointCollectionEntity;
import com.imstargg.storage.db.core.statistics.StatisticsCheckPointCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class StatisticsCheckPointer {

    private final StatisticsCheckPointCollectionJpaRepository repository;

    public StatisticsCheckPointer(
            StatisticsCheckPointCollectionJpaRepository statisticsCheckPointCollectionJpaRepository
    ) {
        this.repository = statisticsCheckPointCollectionJpaRepository;
    }

    public StatisticsCheckPointCollectionEntity get(String key, LocalDate battleDate) {
        return repository.findByKeyAndBattleDate(key, battleDate)
                .orElseGet(() -> repository.save(new StatisticsCheckPointCollectionEntity(key, battleDate)));
    }


    public void save(StatisticsCheckPointCollectionEntity entity) {
        repository.save(entity);
    }
}
