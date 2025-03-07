package com.imstargg.core.api.controller.v1.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imstargg.core.domain.player.PlayerBattle;
import com.imstargg.core.enums.BattleMode;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.List;

public record PlayerBattleResponse(
        OffsetDateTime battleTime,
        PlayerBattleEventResponse event,
        BattleMode mode,
        BattleType type,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable BattleResult result,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable Integer duration,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable Integer rank,
        @JsonInclude(JsonInclude.Include.NON_NULL) @Nullable Integer trophyChange,
        @Nullable String starPlayerTag,
        List<List<BattlePlayerResponse>> teams
) {

    public static PlayerBattleResponse of(PlayerBattle battle) {
        return new PlayerBattleResponse(
                battle.battleTime(),
                PlayerBattleEventResponse.of(battle.event()),
                battle.mode(),
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
