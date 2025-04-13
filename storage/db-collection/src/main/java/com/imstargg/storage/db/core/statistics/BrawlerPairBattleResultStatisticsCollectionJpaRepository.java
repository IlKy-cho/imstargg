package com.imstargg.storage.db.core.statistics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BrawlerPairBattleResultStatisticsCollectionJpaRepository
        extends JpaRepository<BrawlerPairBattleResultStatisticsCollectionEntity, Long>,
        BrawlerPairBattleResultStatisticsCollectionJpaRepositoryCustom {

    List<BrawlerPairBattleResultStatisticsCollectionEntity> findAllByBattleDate(LocalDate battleDate);
}
