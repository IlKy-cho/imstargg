package com.imstargg.core.domain;

import com.imstargg.core.domain.brawlstars.BattleEvent;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record PlayerBattle(
        LocalDateTime battleTime,
        @Nullable BattleEvent event,
        BattleType type,
        @Nullable BattleResult result,
        @Nullable Integer duration,
        @Nullable Integer rank,
        @Nullable Integer trophyChange,
        @Nullable BrawlStarsTag starPlayerTag,
        List<List<BattlePlayer>> teams
) {
}
