package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BattleEventCollectionJpaRepository extends JpaRepository<BattleEventCollectionEntity, Long> {

    Optional<BattleEventCollectionEntity> findByBrawlStarsId(long brawlStarsId);
}
