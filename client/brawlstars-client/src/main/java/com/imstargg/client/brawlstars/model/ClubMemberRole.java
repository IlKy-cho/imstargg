package com.imstargg.client.brawlstars.model;

public enum ClubMemberRole {

    NOT_MEMBER("notMember"),
    MEMBER("member"),
    PRESIDENT("president"),
    SENIOR("senior"),
    VICE_PRESIDENT("vicePresident"),
    UNKNOWN("unknown")
    ;

    private final String code;

    ClubMemberRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
