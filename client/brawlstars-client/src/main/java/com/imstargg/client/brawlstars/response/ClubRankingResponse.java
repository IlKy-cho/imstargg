package com.imstargg.client.brawlstars.response;

public record ClubRankingResponse(
        String tag,
        String name,
        long badgeId,
        int trophies,
        int rank,
        int memberCount
) {
}
