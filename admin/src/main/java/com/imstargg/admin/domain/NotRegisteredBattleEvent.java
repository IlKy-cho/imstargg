package com.imstargg.admin.domain;

public record NotRegisteredBattleEvent(
        long brawlStarsId,
        String mode,
        String map
) {
}
