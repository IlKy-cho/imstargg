package com.imstargg.core.enums;

public enum BattleType {

    RANKED("ranked"),
    SOLO_RANKED("soloRanked"),
    ;

    private final String name;

    BattleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
