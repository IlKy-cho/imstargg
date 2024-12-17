package com.imstargg.core.enums;

import java.util.function.UnaryOperator;

public enum BrawlStarsImageType {

    BRAWLER_PROFILE(brawlStarsId -> "brawlers." + brawlStarsId + ".profile"),
    ;

    private final UnaryOperator<String> codeFunction;

    BrawlStarsImageType(UnaryOperator<String> codeFunction) {
        this.codeFunction = codeFunction;
    }

    public String code(long brawlStarsId) {
        return codeFunction.apply(String.valueOf(brawlStarsId));
    }

    public String code(String mapCode) {
        return codeFunction.apply(mapCode);
    }
}
