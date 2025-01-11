package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BrawlerEnemyBattleResultStatisticsJpaRepository
        extends JpaRepository<BrawlerEnemyBattleResultStatisticsEntity, Long> {

    List<BrawlerEnemyBattleResultStatisticsEntity> findAllByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndDuplicateBrawler(
            long eventBrawlStarsId, LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            boolean duplicateBrawler
    );
}
