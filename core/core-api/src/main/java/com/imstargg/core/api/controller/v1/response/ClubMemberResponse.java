package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.club.ClubMember;
import com.imstargg.core.enums.ClubMemberRole;
import jakarta.annotation.Nullable;

public record ClubMemberResponse(
        String tag,
        String name,
        @Nullable String nameColor,
        ClubMemberRole role,
        int trophies,
        long iconId
) {

    public static ClubMemberResponse from(ClubMember clubMember) {
        return new ClubMemberResponse(
                clubMember.tag().value(),
                clubMember.name(),
                clubMember.nameColor(),
                clubMember.role(),
                clubMember.trophies(),
                clubMember.iconId().value()
        );
    }
}
