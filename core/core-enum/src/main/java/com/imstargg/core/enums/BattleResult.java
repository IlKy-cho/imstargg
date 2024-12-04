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

    private final String code;

    private static final Map<String, BattleResult> ENUM_BY_CODE = Arrays.stream(BattleResult.values())
            .filter(e -> e != NOT_FOUND)
            .collect(Collectors.toMap(BattleResult::getCode, Function.identity()));

    public static BattleResult find(String code) {
        return ENUM_BY_CODE.getOrDefault(code, NOT_FOUND);
    }

    BattleResult(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
