package com.imstargg.core.enums;

public enum ClubType {

    OPEN("open"),
    INVITE_ONLY("inviteOnly"),
    ;

    private final String code;

    ClubType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
