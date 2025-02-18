package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BrawlersBattleRankStatisticsJpaRepository extends JpaRepository<BrawlersBattleRankStatisticsEntity, Long> {

    List<BrawlersBattleRankStatisticsEntity> findAllByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndBrawlerBrawlStarsIdAndBrawlersNum(
            long eventBrawlStarsId,
            LocalDate battleDate,
            TrophyRange trophyRange,
            long brawlerBrawlStarsId,
            int brawlersNum
    );
}
