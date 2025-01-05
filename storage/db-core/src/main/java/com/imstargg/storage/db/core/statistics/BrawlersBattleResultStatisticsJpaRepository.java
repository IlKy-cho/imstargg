package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTier;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface BrawlersBattleResultStatisticsJpaRepository extends JpaRepository<BrawlersBattleResultStatisticsEntity, Long> {

    Slice<BrawlersBattleResultStatisticsEntity> findSliceByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlersNumAndDuplicateBrawler(
            long eventBrawlStarsId, LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTier soloRankTier,
            int brawlersNum, boolean duplicateBrawler,
            Pageable pageable
    );
}
