package com.imstargg.storage.db.core;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BattleJpaRepository extends JpaRepository<BattleEntity, Long> {

    List<BattleEntity> findAllByPlayerPlayerIdAndDeletedFalseOrderByBattleTimeDesc(long playerId, Pageable pageable);
}
