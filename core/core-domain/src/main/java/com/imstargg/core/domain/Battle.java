package com.imstargg.core.domain;

import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;

import java.time.LocalDateTime;
import java.util.List;

public record Battle(
        LocalDateTime battleTime,
        BattleEvent event,
        BattleType type,
        BattleResult result,
        int duration,
        BrawlStarsTag starPlayerTag,
        List<BattlePlayer> enemyTeam,
        List<BattlePlayer> myTeam
) {
}
