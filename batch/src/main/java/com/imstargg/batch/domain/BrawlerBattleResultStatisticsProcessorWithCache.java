package com.imstargg.batch.domain;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.imstargg.storage.db.core.statistics.QBrawlerBattleResultStatisticsCollectionEntity.brawlerBattleResultStatisticsCollectionEntity;


public class BrawlerBattleResultStatisticsProcessorWithCache {

    private final EntityManagerFactory emf;
    private final ConcurrentHashMap<BrawlerBattleResultKey, BrawlerBattleResultStatisticsCollectionEntity> cache;

    public BrawlerBattleResultStatisticsProcessorWithCache(EntityManagerFactory emf) {
        this.emf = emf;
        this.cache = new ConcurrentHashMap<>();
    }

    public List<BrawlerBattleResultStatisticsCollectionEntity> process(Collection<BattleCollectionEntity> battles) {
        Map<BrawlerBattleResultKey, BrawlerBattleResultStatisticsCollectionEntity> result = new HashMap<>();
        battles.forEach(battle -> {
            List<BattleCollectionEntityTeamPlayer> myTeam = battle.findMyTeam();
            List<BattleCollectionEntityTeamPlayer> enemyTeam = battle.findEnemyTeam();
            BattleResult battleResult = BattleResult.map(battle.getResult());

            myTeam.forEach(myPlayer -> enemyTeam.forEach(enemyPlayer -> {
                BrawlerBattleResultKey myPlayerKey = BrawlerBattleResultKey.of(battle, myPlayer, enemyPlayer);
                BrawlerBattleResultStatisticsCollectionEntity myPlayerResult = getBrawlerBattleResult(myPlayerKey);
                myPlayerResult.countUp(battleResult);
                if (myPlayer.getBrawlStarsTag().equals(battle.getStarPlayerBrawlStarsTag())) {
                    myPlayerResult.starPlayer();
                }
                result.put(myPlayerKey, myPlayerResult);

                BrawlerBattleResultKey enemyPlayerKey = BrawlerBattleResultKey.of(battle, enemyPlayer, myPlayer);
                BrawlerBattleResultStatisticsCollectionEntity enemyPlayerResult = getBrawlerBattleResult(enemyPlayerKey);
                enemyPlayerResult.countUp(battleResult.opposite());
                if (enemyPlayer.getBrawlStarsTag().equals(battle.getStarPlayerBrawlStarsTag())) {
                    enemyPlayerResult.starPlayer();
                }
                result.put(enemyPlayerKey, enemyPlayerResult);
            }));
        });

        return List.copyOf(result.values());
    }

    private BrawlerBattleResultStatisticsCollectionEntity getBrawlerBattleResult(BrawlerBattleResultKey key) {
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        loadCache(key);
        return cache.computeIfAbsent(key, k -> new BrawlerBattleResultStatisticsCollectionEntity(
                k.eventBrawlStarsId(),
                k.battleDate(),
                k.soloRankTierRange(),
                k.trophyRange(),
                k.duplicateBrawler(),
                k.brawlerBrawlStarsId(),
                k.enemyBrawlerBrawlStarsId()
        ));
    }

    private void loadCache(BrawlerBattleResultKey key) {
        JPAQueryFactoryUtils.getQueryFactory(emf)
                .selectFrom(brawlerBattleResultStatisticsCollectionEntity)
                .where(
                        brawlerBattleResultStatisticsCollectionEntity.eventBrawlStarsId.eq(key.eventBrawlStarsId()),
                        brawlerBattleResultStatisticsCollectionEntity.battleDate.eq(key.battleDate())
                )
                .fetch()
                .forEach(brawlerBattleResult ->
                        cache.put(BrawlerBattleResultKey.of(brawlerBattleResult), brawlerBattleResult)
                );
    }

}
