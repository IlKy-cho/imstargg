package com.imstargg.batch.job;

import com.imstargg.batch.job.support.querydsl.QuerydslZeroPagingItemReader;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.function.Function;

public class PlayerUpdateJobItemReader extends QuerydslZeroPagingItemReader<PlayerCollectionEntity> {

    public PlayerUpdateJobItemReader(
            EntityManagerFactory entityManagerFactory,
            int pageSize,
            Function<JPAQueryFactory, JPAQuery<PlayerCollectionEntity>> queryFunction
    ) {
        super(entityManagerFactory, pageSize, queryFunction);
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
}
