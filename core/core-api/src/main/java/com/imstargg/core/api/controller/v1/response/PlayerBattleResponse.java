package com.imstargg.core.api.controller.v1.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imstargg.core.domain.PlayerBattle;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;

public record PlayerBattleResponse(
        LocalDateTime battleTime,
        @Nullable BattleEventResponse event,
        BattleType type,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable BattleResult result,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable Integer duration,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable Integer rank,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable Integer trophyChange,
        @Nullable String starPlayerTag,
        List<List<BattlePlayerResponse>> teams
) {

    public static PlayerBattleResponse from(PlayerBattle battle) {
        return new PlayerBattleResponse(
                battle.battleTime(),
                battle.event() == null ? null : BattleEventResponse.from(battle.event()),
                battle.type(),
                battle.result(),
                battle.duration(),
                battle.rank(),
                battle.trophyChange(),
                battle.starPlayerTag() == null ? null : battle.starPlayerTag().value(),
                battle.teams().stream()
                        .map(team -> team.stream()
                                .map(BattlePlayerResponse::from)
                                .toList())
                        .toList()
        );
    }

}
