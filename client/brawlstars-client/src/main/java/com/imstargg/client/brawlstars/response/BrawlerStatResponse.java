package com.imstargg.client.brawlstars.response;

import java.util.List;

public record BrawlerStatResponse(
        long id,
        String name,
        int power,
        int rank,
        int trophies,
        int highestTrophies,
        List<GearStatResponse> gears,
        List<AccessoryResponse> gadgets,
        List<StarPowerResponse> starPowers
) {
}
