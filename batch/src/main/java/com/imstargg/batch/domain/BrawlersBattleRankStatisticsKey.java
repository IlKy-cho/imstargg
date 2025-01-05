package com.imstargg.batch.domain;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;

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
            BattleCollectionEntity battle, List<BattleCollectionEntityTeamPlayer> players
    ) {
        BattleType battleType = BattleType.find(battle.getType());
        List<Long> brawlerBrawlStarsIds = players.stream()
                .map(player -> player.getBrawler().getBrawlStarsId())
                .toList();
        BrawlerIdHash brawlerIdHash = BrawlerIdHash.of(brawlerBrawlStarsIds);
        return players.stream().map(player -> new BrawlersBattleRankStatisticsKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                battle.getBattleTime().toLocalDate(),
                player.getBrawler().getBrawlStarsId(),
                brawlerIdHash.value(),
                TrophyRange.of(battleType, player.getBrawler().getTrophies())
        )).toList();
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
