package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.Message;
import com.imstargg.core.domain.brawlstars.BattleEvent;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.Language;
import jakarta.annotation.Nullable;

import java.util.Optional;

public record BattleEventResponse(
        long id,
        BattleEventMode mode,
        @Nullable String mapName,
        @Nullable String mapImagePath
) {

    public static BattleEventResponse from(BattleEvent event) {
        return new BattleEventResponse(
                event.id().value(),
                event.mode(),
                Optional.ofNullable(event.map().names())
                        .flatMap(names -> names.find(Language.KOREAN))
                        .map(Message::content)
                        .orElse(null),
                event.map().imagePath()
        );
    }
}
