package com.imstargg.storage.db.core.statistics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BrawlerPairBattleRankStatisticsCollectionJpaRepository
        extends JpaRepository<BrawlerPairBattleRankStatisticsCollectionEntity, Long> {

    List<BrawlerPairBattleRankStatisticsCollectionEntity> findAllByBattleDate(LocalDate battleDate);
}
