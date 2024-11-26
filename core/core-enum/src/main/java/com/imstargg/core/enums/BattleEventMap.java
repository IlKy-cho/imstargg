package com.imstargg.core.enums;

public enum BattleEventMap {

    NOT_FOUND(null),


    ;

    private final String name;

    BattleEventMap(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
