package com.imstargg.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrawlerCollectionJpaRepository extends JpaRepository<BrawlerCollectionEntity, String> {

    Optional<BrawlerCollectionEntity> findByBrawlStarsId(long brawlStarsId);
}
