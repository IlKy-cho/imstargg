package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.BattleEventUpdate;
import com.imstargg.admin.domain.NewMessageCollection;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotEmpty;

import java.util.Map;

public record BattleEventUpdateRequest(
        @NotEmpty Map<Language, String> names
) {

    public BattleEventUpdate toBattleEventUpdate() {
        return new BattleEventUpdate(new NewMessageCollection(names));
    }
}
