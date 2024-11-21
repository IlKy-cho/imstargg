package com.imstargg.client.brawlstars.response;

import jakarta.annotation.Nullable;

public record PlayerRankingResponse(
        String tag,
        String name,
        String nameColor,
        PlayerIconResponse icon,
        int trophies,
        int rank,
        @Nullable PlayerRankingClubResponse club
) {
}
