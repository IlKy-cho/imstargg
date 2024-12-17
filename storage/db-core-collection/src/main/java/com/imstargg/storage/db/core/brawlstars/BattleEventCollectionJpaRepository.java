package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleEventCollectionJpaRepository
        extends JpaRepository<BattleEventCollectionEntity, Long>, BattleEventCollectionJpaRepositoryCustom {
}
