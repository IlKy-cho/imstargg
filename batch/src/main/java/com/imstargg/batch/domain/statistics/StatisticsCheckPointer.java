package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.statistics.StatisticsCheckPointCollectionEntity;
import com.imstargg.storage.db.core.statistics.StatisticsCheckPointCollectionJpaRepository;
import jakarta.annotation.Nullable;

public class StatisticsCheckPointer {

    private final String key;

    private final StatisticsCheckPointCollectionJpaRepository repository;

    @Nullable
    private StatisticsCheckPointCollectionEntity entity;

    public StatisticsCheckPointer(
            String key,
            StatisticsCheckPointCollectionJpaRepository statisticsCheckPointCollectionJpaRepository
    ) {
        this.key = key;
        this.repository = statisticsCheckPointCollectionJpaRepository;
    }

    public long getLastBattleId() {
        return getEntity().getLastBattleId();
    }

    public void update(long battleId) {
        getEntity().updateLastBattleId(battleId);
    }

    public void save() {
        repository.save(getEntity());
    }

    private StatisticsCheckPointCollectionEntity getEntity() {
        if (entity == null) {
            entity = repository.findByKey(key)
                    .orElseGet(() -> repository.save(new StatisticsCheckPointCollectionEntity(key)));
        }
        return entity;
    }
}
