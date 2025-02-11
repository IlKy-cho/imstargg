package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.IdHash;
import jakarta.annotation.Nullable;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record BrawlersBattleResultStatisticsKey(
        long eventBrawlStarsId,
        LocalDate battleDate,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        long brawlerBrawlStarsId,
        IdHash brawlerBrawlStarsIdHash
) {

    public static List<BrawlersBattleResultStatisticsKey> of(
            Clock clock,
            BattleCollectionEntity battle,
            List<BattleCollectionEntityTeamPlayer> players
    ) {
        BattleType battleType = BattleType.find(battle.getType());
        List<Long> brawlerBrawlStarsIds = players.stream()
                .map(player -> player.getBrawler().getBrawlStarsId())
                .toList();
        IdHash brawlerIdHash = IdHash.of(brawlerBrawlStarsIds);
        return players.stream().map(player -> new BrawlersBattleResultStatisticsKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                battle.getBattleTime().atZoneSameInstant(clock.getZone()).toLocalDate(),
                TrophyRange.of(battleType, player.getBrawler().getTrophies()),
                SoloRankTierRange.of(battleType, player.getBrawler().getTrophies()),
                player.getBrawler().getBrawlStarsId(),
                brawlerIdHash
        )).toList();
    }

    public static BrawlersBattleResultStatisticsKey of(
            BrawlersBattleResultStatisticsCollectionEntity entity
    ) {
        return new BrawlersBattleResultStatisticsKey(
                entity.getEventBrawlStarsId(),
                entity.getBattleDate(),
                entity.getTrophyRange(),
                entity.getSoloRankTierRange(),
                entity.getBrawlerBrawlStarsId(),
                new IdHash(entity.getBrawlers().getIdHash())
        );
    }

}