package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.NewGadget;
import com.imstargg.admin.domain.NewMessageCollection;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;

public record NewGadgetRequest(
        long brawlStarsId,
        @NotEmpty Map<Language, String> names
) {

    public NewGadget toNewGadget() {
        return new NewGadget(brawlStarsId, new NewMessageCollection(names));
    }
}
