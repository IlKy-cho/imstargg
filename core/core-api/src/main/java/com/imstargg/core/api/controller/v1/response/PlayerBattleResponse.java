package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.BrawlStarsTag;
import com.imstargg.core.domain.PlayerBattle;
import com.imstargg.core.domain.brawlstars.PlayerBattleEvent;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record PlayerBattleResponse(
        LocalDateTime battleTime,
        Event event,
        BattleType type,
        BattleResult result,
        @Nullable Integer duration,
        @Nullable Integer rank,
        @Nullable Integer trophyChange,
        @Nullable BrawlStarsTag starPlayerTag,
        List<List<BattlePlayerResponse>> teams
) {

    public static PlayerBattleResponse from(PlayerBattle battle) {
        return new PlayerBattleResponse(
                battle.battleTime(),
                Event.from(battle.event()),
                battle.type(),
                battle.result(),
                battle.duration(),
                battle.rank(),
                battle.trophyChange(),
                battle.starPlayerTag(),
                battle.teams().stream()
                        .map(team -> team.stream()
                                .map(BattlePlayerResponse::from)
                                .toList())
                        .toList()
        );
    }

    public record Event(
            BrawlStarsId id,
            @Nullable BattleEventMode mode,
            @Nullable String mapCode,
            @Nullable String mapName
    ) {

            public static Event from(PlayerBattleEvent event) {
                return new Event(
                        event.id(),
                        event.mode(),
                        event.map() == null ? null : event.map().code(),
                        event.map() == null ? null : event.map().name()
                );
            }
    }
}
