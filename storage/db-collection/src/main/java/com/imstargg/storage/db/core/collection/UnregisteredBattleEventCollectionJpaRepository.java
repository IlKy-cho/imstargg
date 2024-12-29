package com.imstargg.storage.db.core.collection;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnregisteredBattleEventCollectionJpaRepository
        extends JpaRepository<UnregisteredBattleEventCollectionEntity, Long> {

    Optional<UnregisteredBattleEventCollectionEntity> findByEventBrawlStarsId(long eventBrawlStarsId);
}
