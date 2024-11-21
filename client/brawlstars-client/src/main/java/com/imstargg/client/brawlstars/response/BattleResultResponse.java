package com.imstargg.client.brawlstars.response;

import java.util.List;

public record BattleResultResponse(
        String mode,
        String type,
        String result,
        int duration,
        BattleResultPlayerResponse starPlayer,
        List<List<BattleResultPlayerResponse>> teams
) {
}
