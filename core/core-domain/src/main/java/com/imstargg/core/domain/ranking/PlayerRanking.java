package com.imstargg.core.domain.ranking;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.BrawlStarsTag;
import jakarta.annotation.Nullable;

public record PlayerRanking(
        BrawlStarsTag tag,
        String name,
        @Nullable String nameColor,
        @Nullable String clubName,
        BrawlStarsId iconId,
        int trophies,
        int rank
) {
}
