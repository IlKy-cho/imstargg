package com.imstargg.core.domain;

import com.imstargg.core.enums.BattleMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record PlayerBattle(
        LocalDateTime battleTime,
        BrawlStarsId eventBrawlStarsId,
        BattleMode mode,
        BattleType type,
        @Nullable BattleResult result,
        @Nullable Integer duration,
        @Nullable Integer rank,
        @Nullable Integer trophyChange,
        @Nullable BrawlStarsTag starPlayerTag,
        List<List<BattlePlayer>> teams
) {
}
