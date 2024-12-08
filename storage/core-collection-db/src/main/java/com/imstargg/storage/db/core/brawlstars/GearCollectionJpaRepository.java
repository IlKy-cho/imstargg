package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GearCollectionJpaRepository extends JpaRepository<GearCollectionEntity, Long> {

    Optional<GearCollectionEntity> findByBrawlStarsId(long brawlStarsId);
} 