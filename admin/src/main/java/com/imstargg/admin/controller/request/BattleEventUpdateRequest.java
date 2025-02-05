package com.imstargg.admin.controller.request;

import com.imstargg.admin.domain.BattleEventUpdate;
import jakarta.validation.constraints.NotEmpty;

public record BattleEventUpdateRequest(
        @NotEmpty BattleEventMapUpdateRequest map
) {

    public BattleEventUpdate toBattleEventUpdate() {
        return new BattleEventUpdate(map.toBattleEventMapUpdate());
    }
}
