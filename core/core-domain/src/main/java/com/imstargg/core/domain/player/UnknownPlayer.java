package com.imstargg.core.domain.player;

import com.imstargg.core.domain.BrawlStarsTag;

import java.time.OffsetDateTime;

public record UnknownPlayer(
        BrawlStarsTag tag,
        int notFoundCount,
        OffsetDateTime updatedAt
) {

}
