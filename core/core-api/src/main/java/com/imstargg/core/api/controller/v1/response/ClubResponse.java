package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.club.Club;
import com.imstargg.core.enums.ClubType;

public record ClubResponse(
        String tag,
        String name,
        String description,
        ClubType type,
        long badgeId,
        int requiredTrophies,
        int trophies
) {

    public static ClubResponse from(Club club) {
        return new ClubResponse(
                club.tag().value(),
                club.name(),
                club.description(),
                club.type(),
                club.badgeId().value(),
                club.requiredTrophies(),
                club.trophies()
        );
    }
}
