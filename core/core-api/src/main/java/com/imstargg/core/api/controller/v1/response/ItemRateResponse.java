package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.brawler.ItemRate;

public record ItemRateResponse(
        long id,
        double value
) {

    public static ItemRateResponse of(ItemRate itemRate) {
        return new ItemRateResponse(itemRate.id().value(), itemRate.value());
    }
}
