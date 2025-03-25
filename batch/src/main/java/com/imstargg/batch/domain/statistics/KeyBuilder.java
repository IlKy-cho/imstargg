package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class KeyBuilder {

    private final BattleCollectionEntity battle;
    private final BattleCollectionEntityTeamPlayer player;

    public KeyBuilder(BattleCollectionEntity battle, BattleCollectionEntityTeamPlayer player) {
        this.battle = battle;
        this.player = player;
    }

    public <K> List<K> build(BiFunction<TrophyRange, SoloRankTierRange, K> function) {
        BattleType battleType = BattleType.find(battle.getType());
        List<K> result = new ArrayList<>();
        if (BattleType.RANKED == battleType) {
            TrophyRange.findAll(player.getBrawler().getTrophies()).forEach(trophyRange -> result.add(function.apply(trophyRange, null)));
        } else if (BattleType.SOLO_RANKED == battleType) {
            SoloRankTierRange.findAll(player.getBrawler().getTrophies()).forEach(soloRankTierRange -> result.add(function.apply(null, soloRankTierRange)));
        }
        result.add(function.apply(null, null));

        return List.copyOf(result);
    }
}
