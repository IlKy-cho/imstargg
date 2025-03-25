package com.imstargg.batch.domain.statistics;

import com.imstargg.storage.db.core.player.BattleCollectionEntity;

import java.util.Collection;
import java.util.List;

public class BattleCombinationBuilder {

    private final BattleCollectionEntity battle;

    public BattleCombinationBuilder(BattleCollectionEntity battle) {
        this.battle = battle;
    }

    public List<BattlePlayerPair> pairWithOpponents() {
        return battle.findMe().stream()
                .flatMap(me -> battle.findEnemyTeams().stream()
                        .flatMap(Collection::stream)
                        .map(enemyPlayer -> new BattlePlayerPair(me, enemyPlayer))
                ).toList();
    }

    public List<BattlePlayerPair> pairWithTeam() {
        return battle.findMe().stream()
                .flatMap(myPlayer -> battle.findMyTeam().stream()
                        .map(teamPlayer -> new BattlePlayerPair(myPlayer, teamPlayer))
                ).toList();
    }
}
