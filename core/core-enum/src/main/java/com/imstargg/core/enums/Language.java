package com.imstargg.core.enums;

public enum Language {

    KOREAN("ko"),
    ENGLISH("en"),
    ;

    private final String code;

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
