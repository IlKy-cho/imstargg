package com.imstargg.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BattlePlayerJpaRepository extends JpaRepository<BattlePlayerEntity, Long> {

    List<BattlePlayerEntity> findAllByBattleIdIn(Collection<Long> battleIds);
}
