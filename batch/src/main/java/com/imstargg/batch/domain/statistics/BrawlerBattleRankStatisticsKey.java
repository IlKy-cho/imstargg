package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record BrawlerBattleRankStatisticsKey(
        long eventBrawlStarsId,
        LocalDate battleDate,
        long brawlerBrawlStarsId,
        TrophyRange trophyRange
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
                TrophyRange.of(battleType, myPlayer.getBrawler().getTrophies())
        );
    }

    public static BrawlerBattleRankStatisticsKey of(
            BrawlerBattleRankStatisticsCollectionEntity entity
    ) {
        return new BrawlerBattleRankStatisticsKey(
                entity.getEventBrawlStarsId(),
                entity.getBattleDate(),
                entity.getBrawlerBrawlStarsId(),
                entity.getTrophyRange()
        );
    }
}
