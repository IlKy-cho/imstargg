package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;
import jakarta.annotation.Nullable;

import java.util.Objects;

public record BrawlerEnemyBattleResultStatisticsKey(
        long eventBrawlStarsId,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        long brawlerBrawlStarsId,
        long enemyBrawlerBrawlStarsId
) {

    public static BrawlerEnemyBattleResultStatisticsKey of(
            BattleCollectionEntity battle,
            BattleCollectionEntityTeamPlayer myPlayer,
            BattleCollectionEntityTeamPlayer enemyPlayer
    ) {
        BattleType battleType = BattleType.find(battle.getType());
        return new BrawlerEnemyBattleResultStatisticsKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                TrophyRange.of(battleType, myPlayer.getBrawler().getTrophies()),
                SoloRankTierRange.of(battleType, myPlayer.getBrawler().getTrophies()),
                myPlayer.getBrawler().getBrawlStarsId(),
                enemyPlayer.getBrawler().getBrawlStarsId()
        );
    }

    public static BrawlerEnemyBattleResultStatisticsKey of(BrawlerEnemyBattleResultStatisticsCollectionEntity entity) {
        return new BrawlerEnemyBattleResultStatisticsKey(
                entity.getEventBrawlStarsId(),
                entity.getTrophyRange(),
                entity.getSoloRankTierRange(),
                entity.getBrawlerBrawlStarsId(),
                entity.getEnemyBrawlerBrawlStarsId()
        );
    }
}