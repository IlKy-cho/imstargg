package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerBattleRankStatisticsCollectionEntity;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record BrawlerBattleRankStatisticsKey(
        long eventBrawlStarsId,
        LocalDate battleDate,
        TrophyRange trophyRange,
        long brawlerBrawlStarsId
) {

    public static BrawlerBattleRankStatisticsKey of(
            Clock clock,
            BattleCollectionEntity battle
    ) {
        List<BattleCollectionEntityTeamPlayer> me = battle.findMe();
        if (me.size() != 1) {
            throw new IllegalStateException("myTeamPlayer is not found or duplicated. battleId: " + battle.getId());
        }

        BattleCollectionEntityTeamPlayer myPlayer = me.getFirst();
        BattleType battleType = BattleType.find(battle.getType());
        return new BrawlerBattleRankStatisticsKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                battle.getBattleTime().atZoneSameInstant(clock.getZone()).toLocalDate(),
                TrophyRange.of(battleType, myPlayer.getBrawler().getTrophies()),
                myPlayer.getBrawler().getBrawlStarsId()
        );
    }

    public static BrawlerBattleRankStatisticsKey of(
            BrawlerBattleRankStatisticsCollectionEntity entity
    ) {
        return new BrawlerBattleRankStatisticsKey(
                entity.getEventBrawlStarsId(),
                entity.getBattleDate(),
                entity.getTrophyRange(),
                entity.getBrawlerBrawlStarsId()
        );
    }
}
