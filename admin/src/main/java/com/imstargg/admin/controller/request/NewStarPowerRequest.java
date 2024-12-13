package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.NewMessageCollection;
import com.imstargg.admin.domain.NewStarPower;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;

public record NewStarPowerRequest(
        long brawlStarsId,
        @NotEmpty Map<Language, String> names
) {

    public NewStarPower toNewStarPower() {
        return new NewStarPower(brawlStarsId, new NewMessageCollection(names));
    }
}
