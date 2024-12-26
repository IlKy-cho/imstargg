package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum BattleMode {

    NOT_FOUND("notFound"),

    KNOCKOUT("knockout"),
    GEM_GRAB("gemGrab"),
    HEIST("heist"),
    HOT_ZONE("hotZone"),
    SIEGE("siege"),
    BOUNTY("bounty"),
    BRAWL_BALL("brawlBall"),
    DUELS("duels"),
    SOLO_SHOWDOWN("soloShowdown"),
    DUO_SHOWDOWN("duoShowdown"),
    WIPEOUT("wipeout"),
    VOLLEY_BRAWL("volleyBrawl"),
    TROPHY_THIEVES("trophyThieves"),
    BOSS_FIGHT("bossFight"),
    BIG_GAME("bigGame"),
    BASKET_BRAWL("basketBrawl"),
    ROBO_RUMBLE("roboRumble"),
    PAYLOAD("payload"),
    TAKEDOWN("takedown"),
    HUNTERS("hunters"),
    BOT_DROP("botDrop"),
    HOLD_THE_TROPHY("holdTheTrophy"),
    ;

    static {
        Set<String> codes = new HashSet<>();
        for (BattleMode mode : values()) {
            if (!codes.add(mode.getCode())) {
                throw new IllegalStateException("Duplicate code: " + mode.getCode());
            }
        }
    }

    private final String code;

    private static final Map<String, BattleMode> ENUM_BY_CODE = Arrays.stream(BattleMode.values())
            .filter(e -> e != NOT_FOUND)
            .collect(Collectors.toMap(BattleMode::getCode, Function.identity()));

    public static BattleMode find(String code) {
        return ENUM_BY_CODE.getOrDefault(code, NOT_FOUND);
    }

    BattleMode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
