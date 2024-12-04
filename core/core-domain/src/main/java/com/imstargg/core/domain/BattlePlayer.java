package com.imstargg.core.domain;

import com.imstargg.core.enums.Brawler;
import jakarta.annotation.Nullable;

public record BattlePlayer(
        BrawlStarsTag tag,
        String name,
        Brawler brawler,
        int brawlerPower,
        @Nullable Integer brawlerTrophies,
        @Nullable Integer brawlerTrophyChange
) {
}
