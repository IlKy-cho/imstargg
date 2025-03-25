package com.imstargg.batch.domain.statistics;

import com.imstargg.batch.util.TierRangeUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.SoloRankTierRange;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.player.BattleCollectionEntityTeamPlayer;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerEnemyBattleResultStatisticsCollectionJpaRepository;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class BrawlerEnemyBattleResultStatisticsCollector
        implements StatisticsCollector<BrawlerEnemyBattleResultStatisticsCollectionEntity> {

    private final BattleStatisticsCollectionValidator validator;
    private final BrawlerEnemyBattleResultStatisticsCollectionJpaRepository jpaRepository;
    private final ConcurrentMap<Key, BrawlerEnemyBattleResultStatisticsCollectionEntity> cache = new ConcurrentHashMap<>();

    public BrawlerEnemyBattleResultStatisticsCollector(
            BattleStatisticsCollectionValidator validator,
            BrawlerEnemyBattleResultStatisticsCollectionJpaRepository jpaRepository
    ) {
        this.validator = validator;
        this.jpaRepository = jpaRepository;
    }

    @PostConstruct
    void init(EntityManager em) {
        jpaRepository.findAllByBattleDateGreaterThanEqual(validator.getMinCollectableBattleDate())
                .forEach(entity -> cache.put(Key.of(entity), entity));
    }

    @Override
    public boolean collect(BattleCollectionEntity battle) {
        if (!validator.validateResultStatisticsCollection(battle)) {
            return false;
        }
        BattleResult battleResult = BattleResult.map(battle.getResult());
        new BattleCombinationBuilder(battle).pairWithOpponents().forEach(pair ->
                Key.of(battle, pair.myPlayer(), pair.otherPlayer()).forEach(key ->
                        getBrawlerEnemyBattleResultStats(key).countUp(battleResult)
                )
        );
        return true;
    }

    private BrawlerEnemyBattleResultStatisticsCollectionEntity getBrawlerEnemyBattleResultStats(Key key) {
        return cache.computeIfAbsent(key, k -> Optional.ofNullable(k.trophyRange)
                .map(trophyRange -> new BrawlerEnemyBattleResultStatisticsCollectionEntity(
                        k.eventBrawlStarsId(),
                        k.brawlerBrawlStarsId(),
                        trophyRange,
                        k.battleDate(),
                        k.enemyBrawlerBrawlStarsId()
                )).or(() -> Optional.ofNullable(k.soloRankTierRange())
                        .map(soloRankTierRange -> new BrawlerEnemyBattleResultStatisticsCollectionEntity(
                                k.eventBrawlStarsId(),
                                k.brawlerBrawlStarsId(),
                                soloRankTierRange,
                                k.battleDate(),
                                k.enemyBrawlerBrawlStarsId()
                        )))
                .orElseGet(() -> new BrawlerEnemyBattleResultStatisticsCollectionEntity(
                        k.eventBrawlStarsId(),
                        k.brawlerBrawlStarsId(),
                        k.battleDate(),
                        k.enemyBrawlerBrawlStarsId()
                ))
        );
    }

    @Override
    public void save() {
        jpaRepository.saveAll(cache.values());
    }

    record Key(
            long eventBrawlStarsId,
            long brawlerBrawlStarsId,
            long enemyBrawlerBrawlStarsId,
            @Nullable TrophyRange trophyRange,
            @Nullable SoloRankTierRange soloRankTierRange,
            LocalDate battleDate
    ) {

        static Key of(BrawlerEnemyBattleResultStatisticsCollectionEntity statsEntity) {
            return new Key(
                    statsEntity.getEventBrawlStarsId(),
                    statsEntity.getBrawlerBrawlStarsId(),
                    statsEntity.getEnemyBrawlerBrawlStarsId(),
                    TierRangeUtils.findTrophyRange(statsEntity.getTierRange()).orElse(null),
                    TierRangeUtils.findSoloRankTierRange(statsEntity.getTierRange()).orElse(null),
                    statsEntity.getBattleDate()
            );
        }

        static List<Key> of(
                BattleCollectionEntity battle,
                BattleCollectionEntityTeamPlayer myPlayer,
                BattleCollectionEntityTeamPlayer enemyPlayer
        ) {
            return new KeyBuilder(battle, myPlayer).build((trophyRange, soloRankTierRange) -> new Key(
                    Objects.requireNonNull(battle.getEvent().getBrawlStarsId()),
                    myPlayer.getBrawler().getBrawlStarsId(),
                    enemyPlayer.getBrawler().getBrawlStarsId(),
                    trophyRange,
                    soloRankTierRange,
                    battle.getBattleTime().toLocalDate()
            ));
        }
    }
}
