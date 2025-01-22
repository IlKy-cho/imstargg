package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.PlayerBattleEvent;
import com.imstargg.core.enums.BattleEventMode;
import jakarta.annotation.Nullable;

public record PlayerBattleEventResponse(
        @Nullable Long id,
        @Nullable BattleEventMode mode,
        @Nullable String mapName,
        @Nullable String mapImagePath
) {

    public static PlayerBattleEventResponse of(PlayerBattleEvent event) {
        return new PlayerBattleEventResponse(
                event.id() != null ? event.id().value() : null,
                event.mode(),
                event.map().name(),
                event.map().imagePath()
        );
    }
}
