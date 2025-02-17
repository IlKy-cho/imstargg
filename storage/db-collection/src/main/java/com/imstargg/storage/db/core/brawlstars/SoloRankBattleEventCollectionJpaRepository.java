package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SoloRankBattleEventCollectionJpaRepository extends JpaRepository<SoloRankBattleEventCollectionEntity, Long> {

    Optional<SoloRankBattleEventCollectionEntity> findByEvent(BattleEventCollectionEntity event);
}
