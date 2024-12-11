package com.imstargg.core.domain;

import com.imstargg.core.domain.brawlstars.Brawler;
import jakarta.annotation.Nullable;

public record BattlePlayerBrawler(
        @Nullable Brawler brawler,
        int power,
        @Nullable Integer trophies,
        @Nullable Integer trophyChange
) {
}
