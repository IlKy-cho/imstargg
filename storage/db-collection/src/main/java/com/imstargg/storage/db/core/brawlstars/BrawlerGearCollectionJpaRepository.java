package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrawlerGearCollectionJpaRepository extends JpaRepository<BrawlerGearCollectionEntity, Long> {

    Optional<BrawlerGearCollectionEntity> findByBrawlerBrawlStarsIdAndGearBrawlStarsId(long brawlerBrawlStarsId, long gearBrawlStarsId);

} 