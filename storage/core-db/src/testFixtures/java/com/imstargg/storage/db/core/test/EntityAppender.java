package com.imstargg.storage.db.core.test;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class EntityAppender {

    private final EntityManager em;

    public EntityAppender(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public <T> T append(T entity) {
        em.persist(entity);
        return entity;
    }

    @Transactional
    public <T> List<T> appendAll(T... entities) {
        return appendAll(List.of(entities));
    }

    @Transactional
    public <T> List<T> appendAll(Iterable<T> entities) {

        Assert.notNull(entities, "Entities must not be null");

        List<T> result = new ArrayList<>();

        for (T entity : entities) {
            result.add(append(entity));
        }

        return Collections.unmodifiableList(result);
    }
}
