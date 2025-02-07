package com.imstargg.core.domain.club;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.enums.ClubMemberRole;
import jakarta.annotation.Nullable;

public record ClubMember(
        BrawlStarsTag tag,
        String name,
        @Nullable String nameColor,
        ClubMemberRole role,
        int trophies,
        BrawlStarsId iconId
) {
}
