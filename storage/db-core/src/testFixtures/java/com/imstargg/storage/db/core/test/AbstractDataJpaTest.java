package com.imstargg.storage.db.core.test;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Tag("context")
@DataJpaTest
@Import(TestJpaConfig.class)
public abstract class AbstractDataJpaTest {

    @Autowired
    protected EntityManager em;

    @Autowired
    protected EntityAppender entityAppender;
}
