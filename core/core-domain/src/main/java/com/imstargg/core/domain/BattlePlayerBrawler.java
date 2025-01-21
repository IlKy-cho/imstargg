package com.imstargg.core.domain;

import jakarta.annotation.Nullable;

public record BattlePlayerBrawler(
        BrawlStarsId brawlerId,
        int power,
        @Nullable Integer trophies,
        @Nullable Integer trophyChange
) {
}
