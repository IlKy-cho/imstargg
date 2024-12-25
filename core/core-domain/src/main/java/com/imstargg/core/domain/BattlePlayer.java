package com.imstargg.core.domain;

import com.imstargg.core.enums.SoloRankTier;
import jakarta.annotation.Nullable;

public record BattlePlayer(
        BrawlStarsTag tag,
        String name,
        @Nullable SoloRankTier soloRankTier,
        BattlePlayerBrawler brawler
) {
}
