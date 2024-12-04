package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum BattleResult {

    NOT_FOUND(null),

    DEFEAT("defeat"),
    VICTORY("victory"),
    DRAW("draw"),
    ;

    private static final Map<String, BattleResult> ENUM_BY_CODE = Arrays.stream(BattleResult.values())
            .filter(e -> e != NOT_FOUND)
            .collect(Collectors.toMap(BattleResult::getCode, Function.identity()));

    private final String code;

    BattleResult(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
