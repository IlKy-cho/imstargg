package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.IdHash;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public record BrawlersBattleRankStatisticsKey(
        long eventBrawlStarsId,
        LocalDate battleDate,
        TrophyRange trophyRange,
        long brawlerBrawlStarsId,
        IdHash brawlerBrawlStarsIdHash
) {

    public static List<BrawlersBattleRankStatisticsKey> of(
            Clock clock,
            BattleCollectionEntity battle, List<BattleCollectionEntityTeamPlayer> players
    ) {
        BattleType battleType = BattleType.find(battle.getType());
        List<Long> brawlerBrawlStarsIds = players.stream()
                .map(player -> player.getBrawler().getBrawlStarsId())
                .toList();
        IdHash brawlerIdHash = IdHash.of(brawlerBrawlStarsIds);
        return players.stream().map(player -> new BrawlersBattleRankStatisticsKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                battle.getBattleTime().atZoneSameInstant(clock.getZone()).toLocalDate(),
                TrophyRange.of(battleType, player.getBrawler().getTrophies()),
                player.getBrawler().getBrawlStarsId(),
                brawlerIdHash
        )).toList();
    }

    public static BrawlersBattleRankStatisticsKey of(
            BrawlersBattleRankStatisticsCollectionEntity entity
    ) {
        return new BrawlersBattleRankStatisticsKey(
                entity.getEventBrawlStarsId(),
                entity.getBattleDate(),
                entity.getTrophyRange(),
                entity.getBrawlerBrawlStarsId(),
                new IdHash(entity.getBrawlers().getIdHash())
        );
    }
}
