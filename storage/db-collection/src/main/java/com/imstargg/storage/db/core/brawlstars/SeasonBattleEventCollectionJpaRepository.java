package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeasonBattleEventCollectionJpaRepository
        extends JpaRepository<SeasonBattleEventCollectionEntity, Long> {

    Optional<SeasonBattleEventCollectionEntity> findByBattleEvent(BattleEventCollectionEntity battleEvent);
}
