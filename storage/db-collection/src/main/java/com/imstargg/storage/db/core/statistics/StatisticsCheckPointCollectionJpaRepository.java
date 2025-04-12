package com.imstargg.storage.db.core.statistics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatisticsCheckPointCollectionJpaRepository
        extends JpaRepository<StatisticsCheckPointCollectionEntity, Long> {

    Optional<StatisticsCheckPointCollectionEntity> findByKey(String key);
}
