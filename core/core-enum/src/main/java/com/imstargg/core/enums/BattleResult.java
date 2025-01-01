package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum BattleResult {

    DEFEAT("defeat"),
    VICTORY("victory"),
    DRAW("draw"),
    ;

    private final String code;

    private static final Map<String, BattleResult> ENUM_BY_CODE = Arrays.stream(BattleResult.values())
            .collect(Collectors.toMap(BattleResult::getCode, Function.identity()));

    public static BattleResult map(String code) {
        return ENUM_BY_CODE.get(code);
    }

    BattleResult(String code) {
        this.code = code;
    }

    public BattleResult opposite() {
        return switch (this) {
            case DEFEAT -> VICTORY;
            case VICTORY -> DEFEAT;
            case DRAW -> DRAW;
        };
    }

    public String getCode() {
        return code;
    }
}
