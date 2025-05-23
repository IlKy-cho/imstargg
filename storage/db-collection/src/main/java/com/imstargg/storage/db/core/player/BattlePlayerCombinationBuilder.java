package com.imstargg.storage.db.core.player;

import java.util.ArrayList;
import java.util.List;

public class BattlePlayerCombinationBuilder {

    private final List<BattleCollectionEntityTeamPlayer> players;
    private final List<List<BattleCollectionEntityTeamPlayer>> result = new ArrayList<>();

    public BattlePlayerCombinationBuilder(List<BattleCollectionEntityTeamPlayer> players) {
        this.players = players;
    }

    public List<List<BattleCollectionEntityTeamPlayer>> build() {
        for (int r = 2; r <= players.size(); r++) {
            combination(new ArrayList<>(), 0, r);
        }
        return result;
    }

    private void combination(
            List<BattleCollectionEntityTeamPlayer> temp,
            int start,
            int r
    ) {
        
        if (temp.size() == r) {
            result.add(new ArrayList<>(temp));
            return;
        }
        
        for (int i = start; i < players.size(); i++) {
            temp.add(players.get(i));
            combination(temp, i + 1, r);
            temp.removeLast();
        }
    }
}
