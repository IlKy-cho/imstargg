package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;

public record BattlePlayerPair(
        BattleCollectionEntityTeamPlayer myPlayer,
        BattleCollectionEntityTeamPlayer otherPlayer
) {
}
