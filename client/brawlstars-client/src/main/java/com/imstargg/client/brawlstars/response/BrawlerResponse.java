package com.imstargg.client.brawlstars.response;

import java.util.List;

public record BrawlerResponse(
        long id,
        String name,
        List<StarPowerResponse> starPowers,
        List<AccessoryResponse> gadgets
) {
}
