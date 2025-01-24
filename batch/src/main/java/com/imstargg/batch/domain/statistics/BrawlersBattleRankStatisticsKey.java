package com.imstargg.batch.domain.statistics;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;
import com.imstargg.storage.db.core.statistics.BrawlersBattleRankStatisticsCollectionEntity;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record BrawlersBattleRankStatisticsKey(
        long eventBrawlStarsId,
        LocalDate battleDate,
        long brawlerBrawlStarsId,
        byte[] brawlerBrawlStarsIdHash,
        TrophyRange trophyRange
) {

    public static List<BrawlersBattleRankStatisticsKey> of(
            Clock clock, BattleCollectionEntity battle, List<BattleCollectionEntityTeamPlayer> players
    ) {
        BattleType battleType = BattleType.find(battle.getType());
        List<Long> brawlerBrawlStarsIds = players.stream()
                .map(player -> player.getBrawler().getBrawlStarsId())
                .toList();
        BrawlerIdHash brawlerIdHash = BrawlerIdHash.of(brawlerBrawlStarsIds);
        return players.stream().map(player -> new BrawlersBattleRankStatisticsKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                battle.getBattleTime().atZoneSameInstant(clock.getZone()).toLocalDate(),
                player.getBrawler().getBrawlStarsId(),
                brawlerIdHash.value(),
                TrophyRange.of(battleType, player.getBrawler().getTrophies())
        )).toList();
    }

    public static BrawlersBattleRankStatisticsKey of(
            BrawlersBattleRankStatisticsCollectionEntity entity
    ) {
        return new BrawlersBattleRankStatisticsKey(
                entity.getEventBrawlStarsId(),
                entity.getBattleDate(),
                entity.getBrawlerBrawlStarsId(),
                entity.getBrawlers().getIdHash(),
                entity.getTrophyRange()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BrawlersBattleRankStatisticsKey that = (BrawlersBattleRankStatisticsKey) o;
        return eventBrawlStarsId == that.eventBrawlStarsId
                && brawlerBrawlStarsId == that.brawlerBrawlStarsId
                && Objects.equals(battleDate, that.battleDate)
                && trophyRange == that.trophyRange
                && Objects.deepEquals(brawlerBrawlStarsIdHash, that.brawlerBrawlStarsIdHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                eventBrawlStarsId,
                battleDate,
                brawlerBrawlStarsId,
                Arrays.hashCode(brawlerBrawlStarsIdHash),
                trophyRange
        );
    }

    @Override
    public String toString() {
        return "BrawlersBattleRankStatisticsKey{" +
                "eventBrawlStarsId=" + eventBrawlStarsId +
                ", battleDate=" + battleDate +
                ", brawlerBrawlStarsId=" + brawlerBrawlStarsId +
                ", brawlerBrawlStarsIdHash=" + Arrays.toString(brawlerBrawlStarsIdHash) +
                ", trophyRange=" + trophyRange +
                '}';
    }
}
