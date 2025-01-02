package com.imstargg.batch.domain;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BattleStatisticsCollectionEntityBrawlers;
import com.imstargg.storage.db.core.statistics.BrawlerIdHash;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlersBattleResultStatisticsCollectionEntity.brawlersBattleResultStatisticsCollectionEntity;


public class BrawlersBattleResultStatisticsProcessorWithCache {

    private final EntityManagerFactory emf;
    private final ConcurrentHashMap<BrawlersBattleResultKey, BrawlersBattleResultStatisticsCollectionEntity> cache;

    public BrawlersBattleResultStatisticsProcessorWithCache(EntityManagerFactory emf) {
        this.emf = emf;
        this.cache = new ConcurrentHashMap<>();
    }

    public List<BrawlersBattleResultStatisticsCollectionEntity> process(Collection<BattleCollectionEntity> battles) {
        Map<BrawlersBattleResultKey, BrawlersBattleResultStatisticsCollectionEntity> result = new HashMap<>();
        battles.forEach(battle -> {
            List<BattleCollectionEntityTeamPlayer> myTeam = battle.findMyTeam();
            List<BattleCollectionEntityTeamPlayer> enemyTeam = battle.findEnemyTeam();
            BattleResult battleResult = BattleResult.map(battle.getResult());

            new PlayerCombinationBuilder(myTeam).build().forEach(myPlayers -> {
                List<BrawlersBattleResultKey> myPlayersKeys = BrawlersBattleResultKey.of(battle, myPlayers);
                myPlayersKeys.forEach(myPlayerKey -> {
                    BrawlersBattleResultStatisticsCollectionEntity myPlayerBrawlerBattleResult = getBrawlerBattleResult(myPlayerKey);
                    myPlayerBrawlerBattleResult.countUp(battleResult);
                    result.put(myPlayerKey, myPlayerBrawlerBattleResult);
                });
            });

            new PlayerCombinationBuilder(enemyTeam).build().forEach(enemyPlayers -> {
                List<BrawlersBattleResultKey> enemyPlayersKeys = BrawlersBattleResultKey.of(battle, enemyPlayers);
                enemyPlayersKeys.forEach(enemyPlayerKey -> {
                    BrawlersBattleResultStatisticsCollectionEntity enemyPlayerBrawlerBattleResult = getBrawlerBattleResult(enemyPlayerKey);
                    enemyPlayerBrawlerBattleResult.countUp(battleResult.opposite());
                    result.put(enemyPlayerKey, enemyPlayerBrawlerBattleResult);
                });
            });
        });

        return List.copyOf(result.values());
    }

    private BrawlersBattleResultStatisticsCollectionEntity getBrawlerBattleResult(BrawlersBattleResultKey key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        loadCache(key);
        return cache.computeIfAbsent(key, k -> {
            BrawlerIdHash brawlerIdHash = new BrawlerIdHash(k.brawlStarsIdHash());
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

    private void loadCache(BrawlersBattleResultKey key) {
        JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlersBattleResultStatisticsCollectionEntity)
                .where(
                        brawlersBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(key.eventBrawlStarsId()),
                        brawlersBattleResultStatisticsCollectionEntity.battleDate.eq(key.battleDate())
                )
                .fetch()
                .forEach(brawlerBattleResult ->
                        cache.put(BrawlersBattleResultKey.of(brawlerBattleResult), brawlerBattleResult)
                );
    }

}
