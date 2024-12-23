package com.imstargg.batch.job;

import com.imstargg.batch.job.support.querydsl.QuerydslZeroPagingItemReader;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;

public class PlayerUpdateJobItemReader extends QuerydslZeroPagingItemReader<PlayerCollectionEntity> {

    public PlayerUpdateJobItemReader(
            EntityManagerFactory entityManagerFactory,
            int pageSize
    ) {
        super(entityManagerFactory, pageSize, queryFactory -> queryFactory
                .selectFrom(playerCollectionEntity)
                .orderBy(playerCollectionEntity.trophies.asc())
        );
        setSaveState(false);
    }

    @Override
    protected void fetchQuery(JPQLQuery<PlayerCollectionEntity> query, EntityTransaction tx) {
        List<PlayerCollectionEntity> queryResult = query.fetch();
        for (PlayerCollectionEntity entity : queryResult) {
            entity.initializeBrawlStarsIdToBrawler();
            entityManager.detach(entity);
            results.add(entity);
        }

        if(tx != null) {
            tx.commit();
        }
    }

    @Override
    protected JPAQuery<PlayerCollectionEntity> createQuery() {
        int page = getPage();
        int trophyRangeOrigin = currentPageStartTrophyRangeOrigin(page);
        int trophyRangeBound = currentPageStartTrophyRangeBound(page);
        int startTrophy = ThreadLocalRandom.current().nextInt(trophyRangeOrigin, trophyRangeBound);

        logger.debug("시작 트로피[" + trophyRangeOrigin + ", " + trophyRangeBound + ") : " + startTrophy);

        return super.createQuery()
                .where(
                        playerCollectionEntity.status.in(
                                PlayerStatus.PLAYER_UPDATED, PlayerStatus.NEW, PlayerStatus.DORMANT_RETURNED
                        ),
                        playerCollectionEntity.trophies.goe(startTrophy)
                );
    }

    private int currentPageStartTrophyRangeOrigin(int page) {
        return page * 10000;
    }

    private int currentPageStartTrophyRangeBound(int page) {
        return (1 + page) * 10000;
    }
}
