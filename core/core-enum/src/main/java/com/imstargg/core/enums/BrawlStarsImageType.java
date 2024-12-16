package com.imstargg.core.enums;

import java.util.function.UnaryOperator;

public enum BrawlStarsImageType {

    BRAWLER_PROFILE(brawlStarsId -> "brawlers." + brawlStarsId + ".profile"),
    ;

    private final UnaryOperator<String> codeFunction;

    BrawlStarsImageType(UnaryOperator<String> codeFunction) {
        this.codeFunction = codeFunction;
    }
}
