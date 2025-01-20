package com.imstargg.batch.domain.statistics;

import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;

public abstract class StatisticsCollectorFactory<T> {

    private final EntityManagerFactory entityManagerFactory;

    protected StatisticsCollectorFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public abstract StatisticsCollector<T> create(long eventBrawlStarsId, LocalDate battleDate);

    protected EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }
}
