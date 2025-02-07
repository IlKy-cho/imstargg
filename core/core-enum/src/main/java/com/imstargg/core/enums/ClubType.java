package com.imstargg.core.enums;

public enum ClubType {

    NOT_FOUND("notFound"),

    OPEN("open"),
    INVITE_ONLY("inviteOnly"),
    CLOSED("closed"),
    UNKNOWN("unknown"),
    ;

    private final String code;

    ClubType(String code) {
        this.code = code;
    }

    public static ClubType find(String code) {
        for (ClubType clubType : values()) {
            if (clubType.code.equals(code)) {
                return clubType;
            }
        }
        return NOT_FOUND;
    }

    public String getCode() {
        return code;
    }
}
