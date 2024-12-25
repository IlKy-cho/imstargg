package com.imstargg.storage.db.core;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleJpaRepository extends JpaRepository<BattleEntity, Long> {

    Slice<BattleEntity> findAllByPlayerPlayerIdAndDeletedFalseOrderByBattleTimeDesc(long playerId, Pageable pageable);
}
