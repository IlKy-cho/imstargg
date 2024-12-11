package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GearJpaRepository extends JpaRepository<GearEntity, Long> {

    Optional<GearEntity> findByBrawlStarsId(long brawlStarsId);
}
