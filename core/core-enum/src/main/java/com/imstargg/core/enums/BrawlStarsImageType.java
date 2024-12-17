package com.imstargg.core.enums;

import java.util.function.UnaryOperator;

public enum BrawlStarsImageType {

    BRAWLER_PROFILE(brawlStarsId -> "brawlers." + brawlStarsId + ".profile"),
    GEAR(brawlStarsId -> "brawlers." + brawlStarsId + ".gear"),
    BATTLE_MAP(mapCode -> "maps." + mapCode)
    ;

    private final UnaryOperator<String> codeFunction;

    BrawlStarsImageType(UnaryOperator<String> codeFunction) {
        this.codeFunction = codeFunction;
    }

    public String code(String key) {
        return codeFunction.apply(key);
    }
}
