package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BrawlerBattleRankStatisticsJpaRepository extends JpaRepository<BrawlerBattleRankStatisticsEntity, Long> {

    List<BrawlerBattleRankStatisticsEntity> findAllByEventBrawlStarsIdAndBattleDateAndTrophyRange(
            long eventBrawlStarsId, LocalDate battleDate, TrophyRange trophyRange
    );
}
