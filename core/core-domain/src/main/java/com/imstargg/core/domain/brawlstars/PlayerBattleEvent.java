package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.BattleEventMode;
import jakarta.annotation.Nullable;

public record PlayerBattleEvent(
        BrawlStarsId id,
        @Nullable BattleEventMode mode,
        @Nullable BattleMap map
) {

    public static final PlayerBattleEvent UNKNOWN = new PlayerBattleEvent(
            new BrawlStarsId(0), null, null);
}
