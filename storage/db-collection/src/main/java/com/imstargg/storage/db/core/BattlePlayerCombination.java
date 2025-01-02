package com.imstargg.storage.db.core;

public record BattlePlayerCombination(
        BattleCollectionEntityTeamPlayer me,
        BattleCollectionEntityTeamPlayer enemy
) {
}
