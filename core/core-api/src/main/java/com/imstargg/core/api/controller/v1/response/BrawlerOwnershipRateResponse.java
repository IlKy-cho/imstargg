package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.statistics.brawler.BrawlerOwnershipRate;
import jakarta.annotation.Nullable;

import java.util.List;

public record BrawlerOwnershipRateResponse(
        @Nullable ItemRateResponse brawler,
        List<ItemRateResponse> gadgets,
        List<ItemRateResponse> starPowers,
        List<ItemRateResponse> gears
) {

    public static BrawlerOwnershipRateResponse of(BrawlerOwnershipRate ownershipRate) {
        return new BrawlerOwnershipRateResponse(
                ownershipRate.brawler() == null ? null : ItemRateResponse.of(ownershipRate.brawler()),
                ownershipRate.gadgets().stream().map(ItemRateResponse::of).toList(),
                ownershipRate.starPowers().stream().map(ItemRateResponse::of).toList(),
                ownershipRate.gears().stream().map(ItemRateResponse::of).toList()
        );
    }
}
