package com.imstargg.batch.domain;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultCollectionEntity;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.Objects;

public record BrawlerBattleResultKey(
        long eventBrawlStarsId,
        LocalDate battleDate,
        long brawlerBrawlStarsId,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        long enemyBrawlerBrawlStarsId,
        boolean duplicateBrawler
) {

    public static BrawlerBattleResultKey of(
            BattleCollectionEntity battle,
            BattleCollectionEntityTeamPlayer myPlayer,
            BattleCollectionEntityTeamPlayer enemyPlayer
    ) {
        BattleType battleType = BattleType.find(battle.getType());
        return new BrawlerBattleResultKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                battle.getBattleTime().toLocalDate(),
                myPlayer.getBrawler().getBrawlStarsId(),
                TrophyRange.of(battleType, myPlayer.getBrawler().getTrophies()),
                SoloRankTierRange.of(battleType, myPlayer.getBrawler().getTrophies()),
                enemyPlayer.getBrawler().getBrawlStarsId(),
                battle.containsDuplicateBrawler()
        );
    }

    public static BrawlerBattleResultKey of(BrawlerBattleResultCollectionEntity brawlerBattleResult) {
        return new BrawlerBattleResultKey(
                brawlerBattleResult.getEventBrawlStarsId(),
                brawlerBattleResult.getBattleDate(),
                brawlerBattleResult.getBrawlerBrawlStarsId(),
                brawlerBattleResult.getTrophyRange(),
                brawlerBattleResult.getSoloRankTierRange(),
                brawlerBattleResult.getEnemyBrawlerBrawlStarsId(),
                brawlerBattleResult.isDuplicateBrawler()
        );
    }
}