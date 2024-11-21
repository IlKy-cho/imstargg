package com.imstargg.client.brawlstars.model;

public enum ClubType {

    OPEN("open"),
    INVITE_ONLY("inviteOnly"),
    CLOSED("closed"),
    UNKNOWN("unknown"),
    ;

    private final String code;

    ClubType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
