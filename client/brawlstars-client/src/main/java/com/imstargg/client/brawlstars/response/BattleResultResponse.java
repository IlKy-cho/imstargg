package com.imstargg.client.brawlstars.response;

import jakarta.annotation.Nullable;

import java.util.List;

public record BattleResultResponse(
        String mode,
        String type,
        @Nullable String result,
        @Nullable Integer duration,
        @Nullable Integer rank,
        @Nullable Integer trophyChange,
        @Nullable BattleResultPlayerResponse starPlayer,
        @Nullable List<List<BattleResultPlayerResponse>> teams,
        @Nullable List<BattleResultPlayerResponse> players
) {
}
