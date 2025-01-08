package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BattleEventJpaRepository extends JpaRepository<BattleEventEntity, Long> {

    List<BattleEventEntity> findAllByBrawlStarsIdIn(Collection<Long> brawlStarsIds);
}
