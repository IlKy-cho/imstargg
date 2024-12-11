package com.imstargg.core.domain;

import com.imstargg.core.domain.brawlstars.Brawler;
import jakarta.annotation.Nullable;

public record BattlePlayer(
        BrawlStarsTag tag,
        String name,
        @Nullable Brawler brawler,
        int brawlerPower,
        @Nullable Integer brawlerTrophies,
        @Nullable Integer brawlerTrophyChange
) {
}
