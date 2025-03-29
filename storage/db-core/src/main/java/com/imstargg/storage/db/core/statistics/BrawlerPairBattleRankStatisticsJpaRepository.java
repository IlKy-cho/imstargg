package com.imstargg.storage.db.core.statistics;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BrawlerPairBattleRankStatisticsJpaRepository extends JpaRepository<BrawlerPairBattleRankStatisticsEntity, Long> {

    Slice<BrawlerPairBattleRankStatisticsEntity> findSliceByEventBrawlStarsIdAndBrawlerBrawlStarsIdAndTierRangeAndBattleDateGreaterThanEqualAndBattleDateLessThanEqual(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            @Nullable String tierRange,
            LocalDate battleDateStart,
            LocalDate battleDateEnd,
            Pageable pageable
    );
}
