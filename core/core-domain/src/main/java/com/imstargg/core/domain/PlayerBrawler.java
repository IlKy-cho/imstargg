package com.imstargg.core.domain;

import java.util.List;

public record PlayerBrawler(
        BrawlStarsId brawlStarsId,
        List<BrawlStarsId> gearIds,
        List<BrawlStarsId> starPowerIds,
        List<BrawlStarsId> gadgetIds,
        int power,
        int rank,
        int trophies,
        int highestTrophies
) {
}
