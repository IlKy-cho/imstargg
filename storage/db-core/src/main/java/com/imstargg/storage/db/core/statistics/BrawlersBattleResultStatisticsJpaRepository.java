package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrawlersBattleResultStatisticsJpaRepository extends JpaRepository<BrawlersBattleResultStatisticsEntity, Long> {

    Slice<BrawlersBattleResultStatisticsEntity> findSliceBySeasonNumberAndEventBrawlStarsIdAndTrophyRangeAndSoloRankTierRangeAndBrawlersNum(
            int seasonNumber, long eventBrawlStarsId,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            int brawlersNum, boolean duplicateBrawler,
            Pageable pageable
    );

    Slice<BrawlersBattleResultStatisticsEntity> findSliceBySeasonNumberAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsIdAndBrawlersNum(
            int seasonNumber,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            long brawlerBrawlStarsId,
            int brawlersNum,
            Pageable pageable
    );
}
