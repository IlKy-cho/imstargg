package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsId;

import java.util.List;

public record PlayerBrawler(
        BrawlStarsId id,
        List<BrawlStarsId> gearIds,
        List<BrawlStarsId> starPowerIds,
        List<BrawlStarsId> gadgetIds,
        int power,
        int rank,
        int trophies,
        int highestTrophies
) {
}
