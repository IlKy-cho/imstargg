package com.imstargg.client.brawlstars.response;

public record ClubMemberResponse(
        String tag,
        String name,
        String nameColor,
        String role,
        int trophies,
        PlayerIconResponse icon
) {
}
