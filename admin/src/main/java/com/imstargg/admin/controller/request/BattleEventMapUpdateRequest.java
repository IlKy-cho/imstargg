package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.BattleEventMapUpdate;
import com.imstargg.admin.domain.NewMessageCollection;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;

public record BattleEventMapUpdateRequest(
        @NotEmpty Map<Language, String> names
) {

    public BattleEventMapUpdate toBattleEventMapUpdate() {
        return new BattleEventMapUpdate(new NewMessageCollection(names));
    }
}
