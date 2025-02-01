package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum BattleType {

    NOT_FOUND(null),

    RANKED("ranked"),
    SOLO_RANKED("soloRanked"),
    FRIENDLY("friendly"),
    CHALLENGE("challenge"),
    TOURNAMENT("tournament"),
    CHAMPIONSHIP_CHALLENGE("championshipChallenge"),
    ;

    private final String code;

    private static final Map<String, BattleType> ENUM_BY_CODE = Arrays.stream(BattleType.values())
            .filter(e -> e != NOT_FOUND)
            .collect(Collectors.toMap(BattleType::getCode, Function.identity()));

    private static final Set<BattleType> REGULAR_TYPES = Set.of(RANKED, SOLO_RANKED);

    public static BattleType find(String code) {
        return ENUM_BY_CODE.getOrDefault(code, NOT_FOUND);
    }

    public static Set<BattleType> regularTypes() {
        return REGULAR_TYPES;
    }

    BattleType(String code) {
        this.code = code;
    }

    public boolean isRegular() {
        return REGULAR_TYPES.contains(this);
    }

    public String getCode() {
        return code;
    }
}
