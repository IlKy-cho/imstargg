package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BrawlersBattleResultStatisticsJpaRepository extends JpaRepository<BrawlersBattleResultStatisticsEntity, Long> {

    Slice<BrawlersBattleResultStatisticsEntity> findSliceByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlersNum(
            long eventBrawlStarsId, LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            int brawlersNum,
            Pageable pageable
    );

    List<BrawlersBattleResultStatisticsEntity> findAllByEventBrawlStarsIdAndBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsIdAndBrawlersNum(
            long eventBrawlStarsId,
            LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            long brawlerBrawlStarsId,
            int brawlersNum
    );

    Slice<BrawlersBattleResultStatisticsEntity> findSliceByBattleDateAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsIdAndBrawlersNum(
            LocalDate battleDate,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            long brawlerBrawlStarsId,
            int brawlersNum,
            Pageable pageable
    );
}
