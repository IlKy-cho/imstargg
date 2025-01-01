package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.imstargg.storage.db.core.statistics.QBattleStatisticsCollectedCollectionEntity.battleStatisticsCollectedCollectionEntity;
import static java.util.stream.Collectors.toMap;
import static org.springframework.orm.jpa.EntityManagerFactoryUtils.getTransactionalEntityManager;

@Component
public class BattleResultStatisticsCollectedFilter {

    private final EntityManagerFactory emf;

    public BattleResultStatisticsCollectedFilter(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<BattleCollectionEntity> filter(Collection<? extends BattleCollectionEntity> battles) {
        Map<String, BattleCollectionEntity> battleKeyToBattle = battles.stream()
                .collect(toMap(
                        BattleCollectionEntity::getBattleKey,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));

        Set<String> existingBattleKeys = getExistingBattleKeys(battleKeyToBattle);

        return battleKeyToBattle.entrySet().stream()
                .filter(entry -> !existingBattleKeys.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .toList();
    }

    private Set<String> getExistingBattleKeys(Map<String, BattleCollectionEntity> battleKeyToBattle) {
        return new HashSet<>(
                getQueryFactory()
                        .select(battleStatisticsCollectedCollectionEntity.battleKey)
                        .from(battleStatisticsCollectedCollectionEntity)
                        .where(battleStatisticsCollectedCollectionEntity.battleKey.in(battleKeyToBattle.keySet()))
                        .fetch()
        );
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(getTransactionalEntityManager(emf));
    }
}
