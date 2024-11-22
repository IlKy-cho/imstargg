package com.imstargg.core.domain;

import jakarta.annotation.Nullable;

public record BattlePlayer(
        Player player,
        Brawler brawler,
        int brawlerPower,
        @Nullable Integer brawlerTrophies
) {
}
