package com.imstargg.core.domain;

import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.core.enums.SoloRankTier;
import jakarta.annotation.Nullable;

import java.time.Clock;
import java.time.OffsetDateTime;

public record Player(
        PlayerId id,
        BrawlStarsTag tag,
        String name,
        String nameColor,
        long iconId,
        int trophies,
        int highestTrophies,
        @Nullable SoloRankTier soloRankTier,
        @Nullable BrawlStarsTag clubTag,
        OffsetDateTime updatedAt,
        PlayerStatus status
) {

    public boolean isNextUpdateCooldownOver(Clock clock) {
        return status.isNextUpdateCooldownOver(OffsetDateTime.now(clock), updatedAt);
    }
}
