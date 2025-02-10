package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrawlerItemCountJpaRepository extends JpaRepository<BrawlerItemCountEntity, Long> {

    Optional<BrawlerItemCountEntity> findByBrawlerBrawlStarsIdAndItemBrawlStarsIdAndTrophyRange(
            long brawlerBrawlStarsId, long itemBrawlStarsId, TrophyRange trophyRange);
}
