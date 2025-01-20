package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.Objects;

public record BrawlerBattleResultStatisticsKey(
        long eventBrawlStarsId,
        LocalDate battleDate,
        long brawlerBrawlStarsId,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        boolean duplicateBrawler
) {

    public static BrawlerBattleResultStatisticsKey of(
            BattleCollectionEntity battle,
            BattleCollectionEntityTeamPlayer myPlayer
    ) {
        BattleType battleType = BattleType.find(battle.getType());
        return new BrawlerBattleResultStatisticsKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                battle.getBattleTime().toLocalDate(),
                myPlayer.getBrawler().getBrawlStarsId(),
                TrophyRange.of(battleType, myPlayer.getBrawler().getTrophies()),
                SoloRankTierRange.of(battleType, myPlayer.getBrawler().getTrophies()),
                battle.containsDuplicateBrawler()
        );
    }

    public static BrawlerBattleResultStatisticsKey of(
            BrawlerBattleResultStatisticsCollectionEntity entity
    ) {
        return new BrawlerBattleResultStatisticsKey(
                entity.getEventBrawlStarsId(),
                entity.getBattleDate(),
                entity.getBrawlerBrawlStarsId(),
                entity.getTrophyRange(),
                entity.getSoloRankTierRange(),
                entity.isDuplicateBrawler()
        );
    }
}