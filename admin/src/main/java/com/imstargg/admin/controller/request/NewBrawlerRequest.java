package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.NewBrawler;
import com.imstargg.admin.domain.NewMessageCollection;
import com.imstargg.core.enums.BrawlerRarity;
import com.imstargg.core.enums.BrawlerRole;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.EnumMap;
import java.util.List;

public record NewBrawlerRequest(
        long brawlStarsId,
        @NotNull BrawlerRarity rarity,
        @NotNull BrawlerRole role,
        @NotEmpty EnumMap<Language, String> names,
        @NotNull List<NewGadgetRequest> gadgets,
        @NotNull List<NewStarPowerRequest> starPowers
) {

    public NewBrawler toNewBrawler() {
        return new NewBrawler(
                brawlStarsId,
                rarity,
                role,
                new NewMessageCollection(names),
                gadgets.stream().map(NewGadgetRequest::toNewGadget).toList(),
                starPowers.stream().map(NewStarPowerRequest::toNewStarPower).toList()
        );
    }
}
