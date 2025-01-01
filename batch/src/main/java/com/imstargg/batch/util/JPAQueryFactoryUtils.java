package com.imstargg.batch.util;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

public abstract class JPAQueryFactoryUtils {

    public static JPAQueryFactory getQueryFactory(EntityManagerFactory emf) {
        return new JPAQueryFactory(EntityManagerFactoryUtils.getTransactionalEntityManager(emf));
    }

    private JPAQueryFactoryUtils() {
    }
}
