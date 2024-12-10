package com.imstargg.core.enums;

public enum BattleEventMode {

    NOT_FOUND("notFound"),

    SOLO_SHOWDOWN("soloShowdown"),
    DUO_SHOWDOWN("duoShowdown"),
    HEIST("heist"),
    BOUNTY("bounty"),
    SIEGE("siege"),
    GEM_GRAB("gemGrab"),
    BRAWL_BALL("brawlBall"),
    BIG_GAME("bigGame"),
    BOSS_FIGHT("bossFight"),
    ROBO_RUMBLE("roboRumble"),
    TAKE_DOWN("takedown"),
    LONE_STAR("loneStar"),
    PRESENT_PLUNDER("presentPlunder"),
    HOT_ZONE("hotZone"),
    SUPER_CITY_RAMPAGE("superCityRampage"),
    KNOCKOUT("knockout"),
    VOLLEY_BRAWL("volleyBrawl"),
    BASKET_BRAWL("basketBrawl"),
    HOLD_THE_TROPHY("holdTheTrophy"),
    TROPHY_THIEVES("trophyThieves"),
    DUELS("duels"),
    WIPEOUT("wipeout"),
    PAYLOAD("payload"),
    BOT_DROP("botDrop"),
    HUNTERS("hunters"),
    LAST_STAND("lastStand"),
    SNOWTEL_THIEVES("snowtelThieves"),
    PUMPKIN_PLUNDER("pumpkinPlunder"),
    TROPHY_ESCAPE("trophyEscape"),
    WIPEOUT_5V5("wipeout5V5"),
    KNOCKOUT_5V5("knockout5V5"),
    GEM_GRAB_5V5("gemGrab5V5"),
    BRAWL_BALL_5V5("brawlBall5V5"),
    GODZILLA_CITY_SMASH("godzillaCitySmash"),
    PAINT_BRAWL("paintBrawl"),
    TRIO_SHOWDOWN("trioShowdown"),
    ZOMBIE_PLUNDER("zombiePlunder"),
    JELLYFISHING("jellyfishing"),
    TAKEDOWN("takedown"),
    UNKNOWN("unknown"),
    ;

    private final String name;

    BattleEventMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
