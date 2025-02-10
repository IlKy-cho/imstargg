package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BrawlPassSeasonCollectionJpaRepository
        extends JpaRepository<BrawlPassSeasonEntity, Long>, BrawlPassSeasonCollectionJpaRepositoryCustom {
}
