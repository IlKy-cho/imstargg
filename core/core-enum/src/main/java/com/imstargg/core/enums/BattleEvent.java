package com.imstargg.core.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum BattleEvent {

    NOT_FOUND(0, BattleEventMode.NOT_FOUND, BattleEventMap.NOT_FOUND, true),
    ;

    private static final Map<Long, BattleEvent> ENUM_BY_ID = Arrays.stream(BattleEvent.values())
            .filter(starPower -> starPower != NOT_FOUND)
            .collect(Collectors.toMap(BattleEvent::getBrawlStarsId, Function.identity()));

    public static boolean exists(long brawlStarsId) {
        return ENUM_BY_ID.containsKey(brawlStarsId);
    }

    public static BattleEvent find(long brawlStarsId) {
        return ENUM_BY_ID.getOrDefault(brawlStarsId, NOT_FOUND);
    }

    private final long brawlStarsId;
    private final BattleEventMode mode;
    private final BattleEventMap map;
    private final boolean disabled;

    BattleEvent(long brawlStarsId, BattleEventMode mode, BattleEventMap map, boolean disabled) {
        this.brawlStarsId = brawlStarsId;
        this.mode = mode;
        this.map = map;
        this.disabled = disabled;
    }

    public long getBrawlStarsId() {
        return brawlStarsId;
    }

    public BattleEventMode getMode() {
        return mode;
    }
}
