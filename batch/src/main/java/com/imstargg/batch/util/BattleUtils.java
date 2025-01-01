package com.imstargg.batch.util;

import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class BattleUtils {

    public static List<List<BattleCollectionEntityTeamPlayer>> playerCombination(
            List<BattleCollectionEntityTeamPlayer> players) {
        if (players.size() < 2) {
            throw new IllegalArgumentException("플레이어가 2명 이상이어야 합니다.");
        }

        List<List<BattleCollectionEntityTeamPlayer>> result = new ArrayList<>();
        
        // 2명부터 전체 플레이어 수까지의 모든 조합을 구함
        for (int r = 2; r <= players.size(); r++) {
            combination(players, new ArrayList<>(), 0, r, result);
        }
        
        return result;
    }

    private static void combination(
            List<BattleCollectionEntityTeamPlayer> players,
            List<BattleCollectionEntityTeamPlayer> temp,
            int start,
            int r,
            List<List<BattleCollectionEntityTeamPlayer>> result) {
        
        if (temp.size() == r) {
            result.add(new ArrayList<>(temp));
            return;
        }
        
        for (int i = start; i < players.size(); i++) {
            temp.add(players.get(i));
            combination(players, temp, i + 1, r, result);
            temp.removeLast();
        }
    }

    private BattleUtils() {
    }
}
