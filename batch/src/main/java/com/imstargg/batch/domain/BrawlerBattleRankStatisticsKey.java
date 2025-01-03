package com.imstargg.batch.domain;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record BrawlerBattleRankStatisticsKey(
        long eventBrawlStarsId,
        LocalDate battleDate,
        long brawlerBrawlStarsId,
        TrophyRange trophyRange,
        int rank
) {

    public static BrawlerBattleRankStatisticsKey of(
            BattleCollectionEntity battle
    ) {
        List<BattleCollectionEntityTeamPlayer> me = battle.findMe();
        if (me.size() != 1) {
            throw new IllegalStateException("me is not found or duplicated. battleId: " + battle.getId());
        }

        BattleCollectionEntityTeamPlayer myPlayer = me.getFirst();
        BattleType battleType = BattleType.find(battle.getType());
        return new BrawlerBattleRankStatisticsKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                battle.getBattleTime().toLocalDate(),
                myPlayer.getBrawler().getBrawlStarsId(),
                TrophyRange.of(battleType, myPlayer.getBrawler().getTrophies()),
                Objects.requireNonNull(battle.getPlayer().getRank())
        );
    }
}
