package com.imstargg.core.domain;

public record BattlePlayer(
        BrawlStarsTag tag,
        String name,
        BattlePlayerBrawler brawler
) {
}
