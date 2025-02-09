package com.imstargg.storage.db.core.statistics;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrawlerItemCountJpaRepository extends JpaRepository<BrawlerItemCountEntity, Long> {

    Optional<BrawlerItemCountEntity> findByBrawlerBrawlStarsIdAndItemBrawlStarsId(long brawlerBrawlStarsId, long itemBrawlStarsId);
}
