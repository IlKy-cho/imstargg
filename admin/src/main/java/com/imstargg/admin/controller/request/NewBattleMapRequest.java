package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.MessageCollection;
import com.imstargg.admin.domain.NewBattleMap;
import com.imstargg.core.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.EnumMap;

public record NewBattleMapRequest(
        @NotBlank String code,
        @NotEmpty EnumMap<Language, String> names
) {
    public NewBattleMap toNewBattleMap() {
        return new NewBattleMap(code, new MessageCollection(names));
    }
}
