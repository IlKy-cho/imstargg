package com.imstargg.storage.db.core.statistics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BrawlerBattleRankStatisticsCollectionJpaRepository
        extends JpaRepository<BrawlerBattleRankStatisticsCollectionEntity, Long> {

    List<BrawlerBattleRankStatisticsCollectionEntity> findAllByBattleDateGreaterThanEqual(LocalDate battleDate);
}
