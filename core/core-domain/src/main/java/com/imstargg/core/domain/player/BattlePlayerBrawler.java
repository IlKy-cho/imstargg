package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsId;
import jakarta.annotation.Nullable;

public record BattlePlayerBrawler(
        BrawlStarsId brawlerId,
        int power,
        @Nullable Integer trophies,
        @Nullable Integer trophyChange
) {
}
