package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BrawlersBattleRankStatisticsJpaRepository extends JpaRepository<BrawlersBattleRankStatisticsEntity, Long> {

    Slice<BrawlersBattleRankStatisticsEntity> findSliceByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndBrawlersNum(
            long eventBrawlStarsId,
            LocalDate battleDate,
            TrophyRange trophyRange,
            int brawlersNum,
            Pageable pageable
    );
}
