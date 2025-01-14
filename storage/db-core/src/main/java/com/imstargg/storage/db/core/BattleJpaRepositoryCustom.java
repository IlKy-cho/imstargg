package com.imstargg.storage.db.core;

import com.imstargg.core.enums.BattleType;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

interface BattleJpaRepositoryCustom {

    List<Long> findAllDistinctEventBrawlStarsIdsByBattleTypeInAndGreaterThanEqualBattleTime(
            @Nullable Collection<BattleType> battleTypes, @Nullable OffsetDateTime battleTime
    );

    Optional<BattleEntity> findLatestBattleByEventBrawlStarsIdAndBattleTypeIn(
            long eventBrawlStarsId, @Nullable Collection<BattleType> battleTypes
    );
}
