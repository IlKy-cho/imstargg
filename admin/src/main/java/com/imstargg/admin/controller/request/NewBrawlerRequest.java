package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.MessageCollection;
import com.imstargg.admin.domain.NewBrawler;
import com.imstargg.core.enums.BrawlerRarity;
import com.imstargg.core.enums.BrawlerRole;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.EnumMap;

public record NewBrawlerRequest(
        long brawlStarsId,
        @NotNull BrawlerRarity rarity,
        @NotNull BrawlerRole role,
        @NotEmpty EnumMap<Language, String> names
) {

    public NewBrawler toNewBrawler() {
        return new NewBrawler(brawlStarsId, rarity, role, new MessageCollection(names));
    }
}
