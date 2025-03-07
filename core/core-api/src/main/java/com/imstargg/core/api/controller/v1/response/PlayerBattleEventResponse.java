package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.Message;
import com.imstargg.core.domain.PlayerBattleEvent;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.Language;
import jakarta.annotation.Nullable;

import java.util.Optional;

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
                Optional.ofNullable(event.map().names())
                        .flatMap(names -> names.find(Language.KOREAN))
                        .map(Message::content)
                        .orElse(null),
                event.map().imagePath()
        );
    }
}
