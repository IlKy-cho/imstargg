package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.BrawlerUpdate;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.EnumMap;

public record BrawlerUpdateRequest(
        @NotNull @NotEmpty EnumMap<Language, String> names
) {

    public BrawlerUpdate toBrawlerUpdate() {
        return new BrawlerUpdate(names);
    }
}
