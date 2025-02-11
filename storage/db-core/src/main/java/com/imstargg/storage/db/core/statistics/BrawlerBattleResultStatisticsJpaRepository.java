package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrawlerBattleResultStatisticsJpaRepository extends JpaRepository<BrawlerBattleResultStatisticsEntity, Long> {

    List<BrawlerBattleResultStatisticsEntity> findAllBySeasonNumberAndEventBrawlStarsIdAndTrophyRangeAndSoloRankTierRange(
            int seasonNumber, long eventBrawlStarsId,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange
    );

    Slice<BrawlerBattleResultStatisticsEntity> findSliceBySeasonNumberAndTrophyRangeAndSoloRankTierRange(
            int seasonNumber,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            Pageable pageable
    );

    List<BrawlerBattleResultStatisticsEntity> findAllBySeasonNumberAndTrophyRangeAndSoloRankTierRangeAndBrawlerBrawlStarsId(
            int seasonNumber,
            @Nullable TrophyRange trophyRange, @Nullable SoloRankTierRange soloRankTierRange,
            long brawlerBrawlStarsId
    );
}
