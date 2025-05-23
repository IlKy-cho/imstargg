package com.imstargg.storage.db.core.player;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleJpaRepository extends JpaRepository<BattleEntity, Long>, BattleJpaRepositoryCustom {

    Slice<BattleEntity> findAllByPlayerPlayerIdAndDeletedFalseOrderByBattleTimeDesc(long playerId, Pageable pageable);
}
