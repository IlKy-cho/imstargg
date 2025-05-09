package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.BattleMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.List;

public record PlayerBattle(
        OffsetDateTime battleTime,
        PlayerBattleEvent event,
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
