package com.imstargg.storage.db.core;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.imstargg.storage.db.core.QBattleCollectionEntity.battleCollectionEntity;

class BattleCollectionJpaRepositoryCustomImpl implements BattleCollectionJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BattleCollectionJpaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<BattleCollectionEntity> findAllLastBattleByPlayerIdIn(List<Long> playerIds) {
        return queryFactory
                .selectFrom(battleCollectionEntity)
                .where(battleCollectionEntity.id.in(selectLastBattleIds(playerIds)))
                .fetch();
    }

    private static JPQLQuery<Long> selectLastBattleIds(List<Long> playerIds) {
        return JPAExpressions.select(battleCollectionEntity.id.max())
                .from(battleCollectionEntity)
                .where(battleCollectionEntity.player.playerId.in(playerIds))
                .groupBy(battleCollectionEntity.player.playerId);
    }
}
