package com.imstargg.batch.domain;

import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultCollectionEntity;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record BrawlersBattleResultKey(
        long eventBrawlStarsId,
        LocalDate battleDate,
        long brawlerBrawlStarsId,
        byte[] brawlStarsIdHash,
        @Nullable TrophyRange trophyRange,
        @Nullable SoloRankTierRange soloRankTierRange,
        boolean duplicateBrawler
) {

    public static List<BrawlersBattleResultKey> of(
            BattleCollectionEntity battle,
            List<BattleCollectionEntityTeamPlayer> players
    ) {
        BattleType battleType = BattleType.find(battle.getType());
        List<Long> brawlerBrawlStarsIds = players.stream()
                .map(player -> player.getBrawler().getBrawlStarsId())
                .toList();
        BrawlerIdHash brawlerIdHash = BrawlerIdHash.of(brawlerBrawlStarsIds);
        boolean battleContainsDuplicateBrawler = battle.containsDuplicateBrawler();
        return players.stream().map(player -> new BrawlersBattleResultKey(
                Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                battle.getBattleTime().toLocalDate(),
                player.getBrawler().getBrawlStarsId(),
                brawlerIdHash.value(),
                TrophyRange.of(battleType, player.getBrawler().getTrophies()),
                SoloRankTierRange.of(battleType, player.getBrawler().getTrophies()),
                battleContainsDuplicateBrawler
        )).toList();
    }

    public static BrawlersBattleResultKey of(BrawlersBattleResultCollectionEntity brawlersBattleResult) {
        return new BrawlersBattleResultKey(
                brawlersBattleResult.getEventBrawlStarsId(),
                brawlersBattleResult.getBattleDate(),
                brawlersBattleResult.getBrawlerBrawlStarsId(),
                brawlersBattleResult.getBrawlers().getIdHash(),
                brawlersBattleResult.getTrophyRange(),
                brawlersBattleResult.getSoloRankTierRange(),
                brawlersBattleResult.isDuplicateBrawler()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BrawlersBattleResultKey that = (BrawlersBattleResultKey) o;
        return eventBrawlStarsId == that.eventBrawlStarsId
                && brawlerBrawlStarsId == that.brawlerBrawlStarsId
                && duplicateBrawler == that.duplicateBrawler
                && Objects.equals(battleDate, that.battleDate)
                && Objects.deepEquals(brawlStarsIdHash, that.brawlStarsIdHash)
                && trophyRange == that.trophyRange
                && soloRankTierRange == that.soloRankTierRange;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                eventBrawlStarsId,
                battleDate,
                brawlerBrawlStarsId,
                Arrays.hashCode(brawlStarsIdHash),
                trophyRange,
                soloRankTierRange,
                duplicateBrawler
        );
    }

    @Override
    public String toString() {
        return "BrawlersBattleResultKey{" +
                "eventBrawlStarsId=" + eventBrawlStarsId +
                ", battleDate=" + battleDate +
                ", brawlerBrawlStarsId=" + brawlerBrawlStarsId +
                ", brawlStarsIdHash=" + Arrays.toString(brawlStarsIdHash) +
                ", trophyRange=" + trophyRange +
                ", soloRankTierRange=" + soloRankTierRange +
                ", duplicateBrawler=" + duplicateBrawler +
                '}';
    }
}