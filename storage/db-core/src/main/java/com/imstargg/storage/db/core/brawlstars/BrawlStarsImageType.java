package com.imstargg.storage.db.core.brawlstars;

import java.util.function.Function;

public enum BrawlStarsImageType {

    BRAWLER_PROFILE(brawlStarsId -> "brawlers." + brawlStarsId + ".profile"),
    GEAR(brawlStarsId -> "gears." + brawlStarsId),
    BATTLE_MAP(brawlStarsId -> "battle-events." + brawlStarsId + ".map"),
    GADGET(brawlStarsId -> "gadgets." + brawlStarsId),
    STAR_POWER(brawlStarsId -> "starpowers." + brawlStarsId)
    ;

    private final Function<Long, String> codeFunction;

    BrawlStarsImageType(Function<Long, String> codeFunction) {
        this.codeFunction = codeFunction;
    }

    public String code(long brawlStarsId) {
        return codeFunction.apply(brawlStarsId);
    }
}
