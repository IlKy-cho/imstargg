package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.core.enums.SoloRankTier;
import jakarta.annotation.Nullable;

import java.time.Clock;
import java.time.OffsetDateTime;

public record Player(
        BrawlStarsTag tag,
        String name,
        String nameColor,
        BrawlStarsId iconId,
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
