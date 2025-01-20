package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BattleStatisticsCollectionEntityBrawlers;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlersBattleResultStatisticsCollectionEntity.brawlersBattleResultStatisticsCollectionEntity;

public class BrawlersBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlersBattleResultStatisticsCollectionEntity> {

    private final ConcurrentHashMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity> cache;

    public BrawlersBattleResultStatisticsCollector(
            ConcurrentHashMap<BrawlersBattleResultStatisticsKey, BrawlersBattleResultStatisticsCollectionEntity> cache
    ) {
        this.cache = cache;
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!battle.canResultStatisticsCollected()) {
            return false;
        }
        battle.myTeamCombinations()
                .stream()
                .filter(myTeamCombination -> myTeamCombination.players().size() == 2)
                .forEach(myTeamCombination ->
                        BrawlersBattleResultStatisticsKey.of(battle, myTeamCombination.players()).forEach(key -> {
                            var brawlersBattleResultStats = getBrawlersBattleResult(key);
                            brawlersBattleResultStats.countUp(BattleResult.map(battle.getResult()));
                        })
                );
        return true;
    }

    @Override
    public List<BrawlersBattleResultStatisticsCollectionEntity> result() {
        return List.copyOf(cache.values());
    }

    private BrawlersBattleResultStatisticsCollectionEntity getBrawlersBattleResult(BrawlersBattleResultStatisticsKey key) {
        return cache.computeIfAbsent(key, k -> {
            BrawlerIdHash brawlerIdHash = new BrawlerIdHash(k.brawlerBrawlStarsIdHash());
            return new BrawlersBattleResultStatisticsCollectionEntity(
                    k.eventBrawlStarsId(),
                    k.battleDate(),
                    k.soloRankTierRange(),
                    k.trophyRange(),
                    k.duplicateBrawler(),
                    k.brawlerBrawlStarsId(),
                    new BattleStatisticsCollectionEntityBrawlers(
                            brawlerIdHash.num(),
                            brawlerIdHash.value()
                    )
            );
        });
    }

}
