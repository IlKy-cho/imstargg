package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import jakarta.annotation.Nullable;

import java.util.Objects;

public record BrawlerBattleResultStatisticsKey(
        long eventBrawlStarsId,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        long brawlerBrawlStarsId
) {

    public static BrawlerBattleResultStatisticsKey of(
            BattleCollectionEntity battle,
            BattleCollectionEntityTeamPlayer player
    ) {
        BattleType battleType = BattleType.find(battle.getType());
        return new BrawlerBattleResultStatisticsKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                TrophyRange.of(battleType, player.getBrawler().getTrophies()),
                SoloRankTierRange.of(battleType, player.getBrawler().getTrophies()),
                player.getBrawler().getBrawlStarsId()
        );
    }

    public static BrawlerBattleResultStatisticsKey of(
            BrawlerBattleResultStatisticsCollectionEntity entity
    ) {
        return new BrawlerBattleResultStatisticsKey(
                entity.getEventBrawlStarsId(),
                entity.getTrophyRange(),
                entity.getSoloRankTierRange(),
                entity.getBrawlerBrawlStarsId()
        );
    }
}