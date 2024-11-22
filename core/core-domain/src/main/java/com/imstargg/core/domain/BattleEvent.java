package com.imstargg.core.domain;

public record BattleEvent(
        BrawlStarsId id,
        String mode,
        String map
) {
}
