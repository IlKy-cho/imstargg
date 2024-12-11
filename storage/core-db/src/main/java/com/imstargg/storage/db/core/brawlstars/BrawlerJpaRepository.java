package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrawlerJpaRepository extends JpaRepository<BrawlerEntity, Long> {

    Optional<BrawlerEntity> findByBrawlStarsId(long brawlStarsId);
}
