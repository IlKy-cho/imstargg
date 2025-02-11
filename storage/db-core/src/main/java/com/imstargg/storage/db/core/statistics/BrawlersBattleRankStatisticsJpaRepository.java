package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrawlersBattleRankStatisticsJpaRepository extends JpaRepository<BrawlersBattleRankStatisticsEntity, Long> {

    Slice<BrawlersBattleRankStatisticsEntity> findSliceBySeasonNumberAndEventBrawlStarsIdAndTrophyRangeAndBrawlersNum(
            int seasonNumber,
            long eventBrawlStarsId,
            TrophyRange trophyRange,
            int brawlersNum,
            Pageable pageable
    );
}
