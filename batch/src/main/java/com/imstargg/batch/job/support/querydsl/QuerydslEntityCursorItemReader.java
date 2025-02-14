package com.imstargg.batch.job.support.querydsl;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.util.CollectionUtils;

import java.util.function.BiFunction;

/**
 * @author choyoungrae
 */
public class QuerydslEntityCursorItemReader<T> extends QuerydslPagingItemReader<T> {

    @Nullable
    private T lastItem;

    private BiFunction<T, JPAQueryFactory, JPAQuery<T>> queryFunctionBuildFunction;

    public QuerydslEntityCursorItemReader() {
        super();
        setName(getClass().getSimpleName());
    }

    public QuerydslEntityCursorItemReader(
            EntityManagerFactory entityManagerFactory,
            int pageSize,
            BiFunction<T, JPAQueryFactory, JPAQuery<T>> queryFunctionBuildFunction
    ) {
        this.entityManagerFactory = entityManagerFactory;
        setPageSize(pageSize);
        this.queryFunctionBuildFunction = queryFunctionBuildFunction;
        setSaveState(false);
        setTransacted(false);
    }

    @Override
    protected void doReadPage() {
        EntityTransaction tx = getTxOrNull();

        JPQLQuery<T> query = createQuery()
                .limit(getPageSize());

        initResults();

        fetchQuery(query, tx);

        resetLastItem();
    }

    @Override
    protected JPAQuery<T> createQuery() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFunctionBuildFunction.apply(lastItem, queryFactory);
    }

    private void resetLastItem() {
        if (isNotEmptyResults()) {
            lastItem = results.getLast();
        }
    }

    // 조회결과가 Empty이면 results에 null이 담긴다
    private boolean isNotEmptyResults() {
        return !CollectionUtils.isEmpty(results) && results.getFirst() != null;
    }
}
