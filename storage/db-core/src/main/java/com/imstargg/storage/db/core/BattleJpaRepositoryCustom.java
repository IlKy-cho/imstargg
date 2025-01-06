package com.imstargg.storage.db.core;

import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

interface BattleJpaRepositoryCustom {

    List<Long> findAllDistinctEventBrawlStarsIds(@Nullable LocalDate fromDate);

    Optional<BattleEntity> findLatestBattle(long eventBrawlStarsId);
}
