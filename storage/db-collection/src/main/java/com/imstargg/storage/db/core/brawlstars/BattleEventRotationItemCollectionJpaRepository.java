package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BattleEventRotationItemCollectionJpaRepository
        extends JpaRepository<BattleEventRotationItemCollectionEntity, Long> {

    List<BattleEventRotationItemCollectionEntity> findAllByRotationIn(Collection<BattleEventRotationCollectionEntity> rotations);
}
