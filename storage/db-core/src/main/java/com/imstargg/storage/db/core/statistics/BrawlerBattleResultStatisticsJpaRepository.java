package com.imstargg.storage.db.core.statistics;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BrawlerBattleResultStatisticsJpaRepository extends JpaRepository<BrawlerBattleResultStatisticsEntity, Long> {

    Slice<BrawlerBattleResultStatisticsEntity> findSliceByEventBrawlStarsIdAndTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
            long eventBrawlStarsId,
            @Nullable String tierRange,
            LocalDate battleDateStart,
            LocalDate battleDateEnd,
            Pageable pageable
    );

    Slice<BrawlerBattleResultStatisticsEntity> findSliceByTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
            @Nullable String tierRange,
            LocalDate battleDateStart,
            LocalDate battleDateEnd,
            Pageable pageable
    );

    Slice<BrawlerBattleResultStatisticsEntity> findSliceByBrawlerBrawlStarsIdAndTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
            long brawlerBrawlStarsId,
            @Nullable String tierRange,
            LocalDate battleDateStart,
            LocalDate battleDateEnd,
            Pageable pageable
    );
}
