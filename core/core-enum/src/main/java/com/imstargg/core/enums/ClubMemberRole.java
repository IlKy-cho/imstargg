package com.imstargg.core.enums;

public enum ClubMemberRole {

    NOT_FOUND("notFound"),

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

    public static ClubMemberRole find(String code) {
        for (ClubMemberRole clubMemberRole : values()) {
            if (clubMemberRole.code.equals(code)) {
                return clubMemberRole;
            }
        }
        return NOT_FOUND;
    }

    public String getCode() {
        return code;
    }
}
