package com.imstargg.storage.db.core;

public record BattlePlayerCombination(
        BattleCollectionEntityTeamPlayer myTeamPlayer,
        BattleCollectionEntityTeamPlayer enemyTeamPlayer
) {
}
