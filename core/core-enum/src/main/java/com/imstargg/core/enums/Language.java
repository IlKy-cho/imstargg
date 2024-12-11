package com.imstargg.core.enums;

public enum Language {

    KOREAN("ko"),
    ENGLISH("en"),
    ;

    public static final Language DEFAULT = ENGLISH;

    private final String code;

    Language(String code) {
        this.code = code;
    }

    public static Language of(String code) {
        for (Language language : values()) {
            if (language.code.equals(code)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Unknown language code: " + code);
    }

    public String getCode() {
        return code;
    }
}
