package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

interface BattleJpaRepositoryCustom {

    List<Long> findAllDistinctEventBrawlStarsIdsByGreaterThanEqualBattleTime(@Nullable LocalDateTime battleTime);

    Optional<BattleEntity> findLatestBattle(long eventBrawlStarsId);
}
