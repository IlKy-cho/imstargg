package com.imstargg.core.enums;

public enum ClubMemberRole {

    PRESIDENT("president"),
    VICE_PRESIDENT("vicePresident"),
    MEMBER("member"),
    ;

    private final String code;

    ClubMemberRole(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
