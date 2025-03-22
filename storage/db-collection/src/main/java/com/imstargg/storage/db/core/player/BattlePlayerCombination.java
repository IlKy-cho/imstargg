package com.imstargg.storage.db.core.player;

public record BattlePlayerCombination(
        BattleCollectionEntityTeamPlayer myTeamPlayer,
        BattleCollectionEntityTeamPlayer enemyTeamPlayer
) {
}
