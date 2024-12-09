package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrawlerCollectionJpaRepository extends JpaRepository<BrawlerCollectionEntity, Long> {

    Optional<BrawlerCollectionEntity> findByBrawlStarsId(long brawlStarsId);
} 