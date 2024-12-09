package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.BrawlerUpdate;
import com.imstargg.admin.domain.NewMessageCollection;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotEmpty;

import java.util.EnumMap;

public record BrawlerUpdateRequest(
        @NotEmpty EnumMap<Language, String> names
) {

    public BrawlerUpdate toBrawlerUpdate() {
        return new BrawlerUpdate(new NewMessageCollection(names));
    }
}
