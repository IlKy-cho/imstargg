package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SoloRankSeasonCollectionJpaRepository extends JpaRepository<SoloRankSeasonCollectionEntity, Long> {

    Optional<SoloRankSeasonCollectionEntity> findFirstByOrderByNumberDescMonthDesc();
}
