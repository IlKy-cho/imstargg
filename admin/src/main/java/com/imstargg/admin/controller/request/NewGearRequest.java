package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.NewGear;
import com.imstargg.admin.domain.NewMessageCollection;
import com.imstargg.core.enums.GearRarity;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record NewGearRequest(
        long brawlStarsId,
        @NotEmpty Map<Language, String> names,
        @NotNull GearRarity rarity
) {

    public NewGear toNewGear() {
        return new NewGear(brawlStarsId, new NewMessageCollection(names), rarity);
    }
}
