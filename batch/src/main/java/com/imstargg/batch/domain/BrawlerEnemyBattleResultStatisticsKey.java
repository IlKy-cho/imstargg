package com.imstargg.batch.domain;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.Objects;

public record BrawlerEnemyBattleResultStatisticsKey(
        long eventBrawlStarsId,
        LocalDate battleDate,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        long brawlerBrawlStarsId,
        long enemyBrawlerBrawlStarsId,
        boolean duplicateBrawler
) {

    public static BrawlerEnemyBattleResultStatisticsKey of(
            BattleCollectionEntity battle,
            BattleCollectionEntityTeamPlayer myPlayer,
            BattleCollectionEntityTeamPlayer enemyPlayer
    ) {
        BattleType battleType = BattleType.find(battle.getType());
        return new BrawlerEnemyBattleResultStatisticsKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                battle.getBattleTime().toLocalDate(),
                TrophyRange.of(battleType, myPlayer.getBrawler().getTrophies()),
                SoloRankTierRange.of(battleType, myPlayer.getBrawler().getTrophies()),
                myPlayer.getBrawler().getBrawlStarsId(),
                enemyPlayer.getBrawler().getBrawlStarsId(),
                battle.containsDuplicateBrawler()
        );
    }
}