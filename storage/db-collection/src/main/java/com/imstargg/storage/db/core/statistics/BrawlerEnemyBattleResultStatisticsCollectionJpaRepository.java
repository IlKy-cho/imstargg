package com.imstargg.storage.db.core.statistics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BrawlerEnemyBattleResultStatisticsCollectionJpaRepository
        extends JpaRepository<BrawlerEnemyBattleResultStatisticsCollectionEntity, Long> {

    List<BrawlerEnemyBattleResultStatisticsCollectionEntity> findAllByBattleDate(LocalDate battleDate);
}
