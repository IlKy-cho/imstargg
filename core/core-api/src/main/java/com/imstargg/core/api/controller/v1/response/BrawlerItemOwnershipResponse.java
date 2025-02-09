package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.brawler.BrawlerItemOwnership;

import java.util.List;

public record BrawlerItemOwnershipResponse(
        List<ItemRateResponse> gadgets,
        List<ItemRateResponse> starPowers,
        List<ItemRateResponse> gears
) {

    public static BrawlerItemOwnershipResponse of(BrawlerItemOwnership ownershipRate) {
        return new BrawlerItemOwnershipResponse(
                ownershipRate.gadgets().stream().map(ItemRateResponse::of).toList(),
                ownershipRate.starPowers().stream().map(ItemRateResponse::of).toList(),
                ownershipRate.gears().stream().map(ItemRateResponse::of).toList()
        );
    }
}
