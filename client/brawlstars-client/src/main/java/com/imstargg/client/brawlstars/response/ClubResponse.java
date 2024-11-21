package com.imstargg.client.brawlstars.response;

import java.util.List;

public record ClubResponse(
        String tag,
        String name,
        String description,
        String type,
        long badgeId,
        int requiredTrophies,
        int trophies,
        List<ClubMemberResponse> members
) {
}
