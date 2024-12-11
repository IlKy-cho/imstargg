package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BattleEventJpaRepository extends JpaRepository<BattleEventEntity, Long> {

    Optional<BattleEventEntity> findByBrawlStarsId(long brawlStarsId);
}
