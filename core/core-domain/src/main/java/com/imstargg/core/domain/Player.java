package com.imstargg.core.domain;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record Player(
        BrawlStarsTag tag,
        String name,
        String nameColor,
        long iconId,
        int trophies,
        int highestTrophies,
        @Nullable BrawlStarsTag clubTag,
        LocalDateTime updatedAt
) {
}
