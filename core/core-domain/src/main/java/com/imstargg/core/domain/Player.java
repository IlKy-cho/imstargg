package com.imstargg.core.domain;

public record Player(
        BrawlStarsTag tag,
        String name,
        String nameColor,
        long iconId,
        int trophies,
        int highestTrophies
) {
}
