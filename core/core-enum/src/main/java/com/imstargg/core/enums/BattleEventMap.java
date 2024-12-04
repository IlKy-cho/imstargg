package com.imstargg.core.enums;

public enum BattleEventMap {

    NOT_FOUND(null),

    RING_O_BRAWLN("Ring 'o Brawlin"),
    SIZZLING_CHANMBERS("Sizzling Chambers"),
    FLYING_FANTASIES("Flying Fantasies"),
    FLARING_PHOENIX("Flaring Phoenix"),
    ;

    private final String name;

    BattleEventMap(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
