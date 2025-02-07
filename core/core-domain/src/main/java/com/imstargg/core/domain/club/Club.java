package com.imstargg.core.domain.club;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.ClubType;

public record Club(
        BrawlStarsTag tag,
        String name,
        String description,
        ClubType type,
        BrawlStarsId badgeId,
        int requiredTrophies,
        int trophies
) {
}
