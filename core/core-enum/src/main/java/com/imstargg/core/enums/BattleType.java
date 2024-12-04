package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum BattleType {

    NOT_FOUND(null),

    RANKED("ranked"),
    SOLO_RANKED("soloRanked"),
    ;

    private static final Map<String, BattleType> ENUM_BY_CODE = Arrays.stream(BattleType.values())
            .filter(e -> e != NOT_FOUND)
            .collect(Collectors.toMap(BattleType::getCode, Function.identity()));

    private final String code;

    BattleType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
