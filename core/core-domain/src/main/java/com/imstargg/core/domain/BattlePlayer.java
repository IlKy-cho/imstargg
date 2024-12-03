package com.imstargg.core.domain;

import com.imstargg.core.enums.Brawler;
import jakarta.annotation.Nullable;

public record BattlePlayer(
        Player player,
        Brawler brawler,
        int brawlerPower,
        @Nullable Integer brawlerTrophies
) {
}
