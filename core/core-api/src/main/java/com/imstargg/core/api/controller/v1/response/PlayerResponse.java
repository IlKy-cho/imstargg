package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.Player;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record PlayerResponse(
        String tag,
        String name,
        String nameColor,
        long iconId,
        int trophies,
        int highestTrophies,
        @Nullable String clubTag,
        LocalDateTime updatedAt
) {

    public static PlayerResponse from(Player player) {
        return new PlayerResponse(
                player.tag().value(),
                player.name(),
                player.nameColor(),
                player.iconId(),
                player.trophies(),
                player.highestTrophies(),
                player.clubTag() != null ? player.clubTag().value() : null,
                player.updatedAt()
        );
    }
}
