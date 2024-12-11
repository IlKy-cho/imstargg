package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BrawlerGearJpaRepository extends JpaRepository<BrawlerGearEntity, Long> {

    List<BrawlerGearEntity> findAllByBrawlerId(long brawlerId);
}
