package com.imstargg.storage.db.core.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CleanUp {

    private final JdbcTemplate jdbcTemplate;

    private final EntityManager entityManager;

    public CleanUp(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.entityManager = entityManager;
    }

    @Transactional
    public void all() {
        entityManager.getMetamodel().getEntities().forEach(entityType -> {
            String tableName = entityType.getJavaType().getAnnotation(Table.class).name();
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
        });
    }
}
