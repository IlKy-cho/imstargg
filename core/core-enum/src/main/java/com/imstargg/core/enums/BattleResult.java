package com.imstargg.core.enums;

public enum BattleResult {

    DEFEAT("defeat"),
    VICTORY("victory"),
    DRAW("draw"),
    ;

    private final String name;

    BattleResult(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
