package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.BattleEventUpdate;
import jakarta.validation.constraints.NotNull;

public record BattleEventUpdateRequest(
        @NotNull BattleEventMapUpdateRequest map
) {

    public BattleEventUpdate toBattleEventUpdate() {
        return new BattleEventUpdate(map.toBattleEventMapUpdate());
    }
}
