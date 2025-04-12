package com.imstargg.storage.db.core.statistics;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsCheckPointCollectionJpaRepository
        extends JpaRepository<StatisticsCheckPointCollectionEntity, Long> {

}
