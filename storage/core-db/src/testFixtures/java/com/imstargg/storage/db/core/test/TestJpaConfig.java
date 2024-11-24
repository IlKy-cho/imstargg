package com.imstargg.storage.db.core.test;

import jakarta.persistence.EntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@TestConfiguration
@EnableJpaAuditing
public class TestJpaConfig {

    @Bean
    public EntityAppender entityAppender(EntityManager em) {
        return new EntityAppender(em);
    }
}