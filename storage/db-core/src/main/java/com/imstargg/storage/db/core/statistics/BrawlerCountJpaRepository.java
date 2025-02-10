package com.imstargg.storage.db.core.statistics;

import com.imstargg.core.enums.TrophyRange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrawlerCountJpaRepository extends JpaRepository<BrawlerCountEntity, Long> {

    Optional<BrawlerCountEntity> findByBrawlerBrawlStarsIdAndTrophyRange(long brawlerBrawlStarsId, TrophyRange trophyRange);
}
