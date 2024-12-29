package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeasonedBattleEventCollectionJpaRepository
        extends JpaRepository<SeasonedBattleEventCollectionEntity, Long> {

    Optional<SeasonedBattleEventCollectionEntity> findByBattleEvent(BattleEventCollectionEntity battleEvent);
}
