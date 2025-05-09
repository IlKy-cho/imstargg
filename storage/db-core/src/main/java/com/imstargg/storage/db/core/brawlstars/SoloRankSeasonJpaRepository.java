package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SoloRankSeasonJpaRepository extends JpaRepository<SoloRankSeasonEntity, Long> {

    Optional<SoloRankSeasonEntity> findFirstByOrderByNumberDescMonthDesc();
}
