package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Gear {

    // TODO LEVEL, NAME
    UNKNOWN(0, "UNKNOWN", null, null),

    SPEED(62000000, "SPEED", GearRarity.SUPER_RARE, 3),
    HEALTH(62000001, "HEALTH", GearRarity.SUPER_RARE, 3),
    DAMAGE(62000002, "DAMAGE", GearRarity.SUPER_RARE, 3),
    VISION(62000003, "VISION", GearRarity.SUPER_RARE, 3),
    SHIELD(62000004, "SHIELD", GearRarity.SUPER_RARE, 3),
    RELOAD_SPEED(62000005, "RELOAD SPEED", GearRarity.EPIC, 3),
    SUPER_CHARGE(62000006, "SUPER CHARGE", GearRarity.SUPER_RARE, 3),
    THICC_HEAD(62000007, "THICC HEAD", GearRarity.MYTHIC, 3),
    TALK_TO_THE_HAND(62000008, "TALK TO THE HAND", GearRarity.MYTHIC, 3),
    ENDURING_TOXIN(62000009, "ENDURING TOXIN", GearRarity.MYTHIC, 3),
    STICKY_SPIKES(62000010, "STICKY SPIKES", GearRarity.MYTHIC, 3),
    LINGERING_SMOKE(62000011, "LINGERING SMOKE", GearRarity.MYTHIC, 3),
    EXHAUSTING_STORM(62000012, "EXHAUSTING STORM", GearRarity.MYTHIC, 3),
    STICKY_OIL(62000013, "STICKY OIL", GearRarity.MYTHIC, 3),
    PET_POWER(62000014, "PET POWER", GearRarity.EPIC, 3),
    QUADRUPLETS(62000015, "QUADRUPLETS", GearRarity.MYTHIC, 3),
    SUPER_TURRET(62000016, "SUPER TURRET", GearRarity.MYTHIC, 3),
    GADGET_CHARGE(62000017, "GADGET CHARGE", GearRarity.SUPER_RARE, 3),
    BAT_STORM(62000018, "BAT STORM", GearRarity.MYTHIC, 3),

    ;

    private static final Map<Long, Gear> ENUM_BY_ID = Arrays.stream(Gear.values())
            .filter(starPower -> starPower != UNKNOWN)
            .collect(Collectors.toMap(Gear::getBrawlStarsId, Function.identity()));

    public static boolean exists(long brawlStarsId) {
        return ENUM_BY_ID.containsKey(brawlStarsId);
    }

    public static Gear find(long brawlStarsId) {
        return ENUM_BY_ID.getOrDefault(brawlStarsId, UNKNOWN);
    }

    private final long brawlStarsId;
    private final String brawlStarsName;
    private final GearRarity rarity;
    private final Integer level;

    Gear(long brawlStarsId, String brawlStarsName, GearRarity rarity, Integer level) {
        this.brawlStarsId = brawlStarsId;
        this.brawlStarsName = brawlStarsName;
        this.rarity = rarity;
        this.level = level;
    }

    public long getBrawlStarsId() {
        return brawlStarsId;
    }

    public String getBrawlStarsName() {
        return brawlStarsName;
    }

    public GearRarity getRarity() {
        return rarity;
    }

    public Integer getLevel() {
        return level;
    }
}
