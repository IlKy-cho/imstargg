package com.imstargg.client.brawlstars.response;

import jakarta.annotation.Nullable;

import java.util.List;

public record BattleResultResponse(
        String mode,
        String type,
        String result,
        int duration,
        @Nullable Integer trophyChange,
        @Nullable BattleResultPlayerResponse starPlayer,
        List<List<BattleResultPlayerResponse>> teams
) {
}
