package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum BattleEventMode {

    NOT_FOUND("notFound"),

    SOLO_SHOWDOWN("soloShowdown"),
    DUO_SHOWDOWN("duoShowdown"),
    TRIO_SHOWDOWN("trioShowdown"),
    BASKET_BRAWL("basketBrawl"),
    BIG_GAME("bigGame"),
    BOSS_FIGHT("bossFight"),
    BOT_DROP("botDrop"),
    BOUNTY("bounty"),
    BRAWL_BALL("brawlBall"),
    BRAWL_BALL_5V5("brawlBall5V5"),
    DUELS("duels"),
    GEM_GRAB("gemGrab"),
    GEM_GRAB_5V5("gemGrab5V5"),
    HOT_ZONE("hotZone"),
    HUNTERS("hunters"),
    HEIST("heist"),
    JELLYFISHING("jellyfishing"),
    SIEGE("siege"),
    KNOCKOUT("knockout"),
    KNOCKOUT_5V5("knockout5V5"),
    PAINT_BRAWL("paintBrawl"),
    PAYLOAD("payload"),
    PRESENT_PLUNDER("presentPlunder"),
    ROBO_RUMBLE("roboRumble"),
    TAKEDOWN("takedown"),
    TROPHY_ESCAPE("trophyEscape"),
    VOLLEY_BRAWL("volleyBrawl"),
    WIPEOUT("wipeout"),
    WIPEOUT_5V5("wipeout5V5"),
    LONE_STAR("loneStar"),
    SUPER_CITY_RAMPAGE("superCityRampage"),
    HOLD_THE_TROPHY("holdTheTrophy"),
    TROPHY_THIEVES("trophyThieves"),
    LAST_STAND("lastStand"),
    SNOWTEL_THIEVES("snowtelThieves"),
    PUMPKIN_PLUNDER("pumpkinPlunder"),
    GODZILLA_CITY_SMASH("godzillaCitySmash"),
    ZOMBIE_PLUNDER("zombiePlunder"),
    UNKNOWN("unknown"),
    ;

    static {
        Set<String> codes = new HashSet<>();
        for (BattleEventMode mode : values()) {
            if (!codes.add(mode.getCode())) {
                throw new IllegalStateException("Duplicate code: " + mode.getCode());
            }
        }
    }

    private final String code;

    private static final Map<String, BattleEventMode> ENUM_BY_CODE = Arrays.stream(BattleEventMode.values())
            .filter(e -> e != NOT_FOUND)
            .collect(Collectors.toMap(BattleEventMode::getCode, Function.identity()));

    public static BattleEventMode find(String code) {
        return ENUM_BY_CODE.getOrDefault(code, NOT_FOUND);
    }

    public static Set<BattleEventMode> showdowns() {
        return Set.of(SOLO_SHOWDOWN, DUO_SHOWDOWN, TRIO_SHOWDOWN);
    }

    BattleEventMode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
