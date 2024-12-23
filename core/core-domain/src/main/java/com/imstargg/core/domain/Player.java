package com.imstargg.core.domain;

import com.imstargg.core.enums.PlayerStatus;
import jakarta.annotation.Nullable;

import java.time.Clock;
import java.time.LocalDateTime;

public record Player(
        PlayerId id,
        BrawlStarsTag tag,
        String name,
        String nameColor,
        long iconId,
        int trophies,
        int highestTrophies,
        @Nullable BrawlStarsTag clubTag,
        LocalDateTime updatedAt,
        PlayerStatus status
) {

    public boolean isNextUpdateCooldownOver(Clock clock) {
        return status.isNextUpdateCooldownOver(LocalDateTime.now(clock), updatedAt);
    }

    public boolean isRenewing() {
        return status() == PlayerStatus.RENEW_REQUESTED;
    }
}
