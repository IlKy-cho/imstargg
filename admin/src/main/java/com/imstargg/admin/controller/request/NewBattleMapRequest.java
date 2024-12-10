package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.NewBattleMap;
import com.imstargg.admin.domain.NewMessageCollection;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotEmpty;

import java.util.EnumMap;

public record NewBattleMapRequest(
        @NotEmpty EnumMap<Language, String> names
) {
    public NewBattleMap toNewBattleMap() {
        return new NewBattleMap(new NewMessageCollection(names));
    }
}
