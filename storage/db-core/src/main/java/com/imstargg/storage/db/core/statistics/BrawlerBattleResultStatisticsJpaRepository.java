package com.imstargg.storage.db.core.statistics;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BrawlerBattleResultStatisticsJpaRepository extends JpaRepository<BrawlerBattleResultStatisticsEntity, Long> {

    Slice<BrawlerBattleResultStatisticsEntity> findAllByEventBrawlStarsIdAndBattleDate(
            long eventBrawlStarsId, LocalDate battleDate, Pageable pageable
    );
}
