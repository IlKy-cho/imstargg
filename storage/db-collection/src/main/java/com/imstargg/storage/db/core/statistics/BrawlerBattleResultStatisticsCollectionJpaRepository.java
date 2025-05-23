package com.imstargg.storage.db.core.statistics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BrawlerBattleResultStatisticsCollectionJpaRepository
        extends JpaRepository<BrawlerBattleResultStatisticsCollectionEntity, Long> {

    List<BrawlerBattleResultStatisticsCollectionEntity> findAllByBattleDate(LocalDate battleDate);
}
