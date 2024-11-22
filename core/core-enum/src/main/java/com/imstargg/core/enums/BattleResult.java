package com.imstargg.core.enums;

public enum BattleResult {

    DEFEAT("defeat"),
    VICTORY("victory"),
    ;

    private final String code;

    BattleResult(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
