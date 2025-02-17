package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BattleEventRotationItemJpaRepository extends JpaRepository<BattleEventRotationItemEntity, Long> {

    List<BattleEventRotationItemEntity> findAllByBattleEventRotationId(long battleEventRotationId);
}
