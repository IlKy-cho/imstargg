package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.GearUpdate;
import com.imstargg.admin.domain.NewMessageCollection;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotEmpty;

import java.util.EnumMap;

public record GearUpdateRequest(
        @NotEmpty EnumMap<Language, String> names
) {

    public GearUpdate toGearUpdate() {
        return new GearUpdate(new NewMessageCollection(new EnumMap<>(names)));
    }
}
