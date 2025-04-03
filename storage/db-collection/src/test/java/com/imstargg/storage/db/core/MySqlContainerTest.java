package com.imstargg.storage.db.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@TestPropertySource(properties = {
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "spring.datasource.url=jdbc:tc:mysql:8.0.36:///test-imstargg",
        "spring.datasource.username=root",
        "spring.datasource.password=1234",
        "spring.sql.init.mode=always",
        "spring.jpa.hibernate.ddl-auto=none",
})
@Testcontainers
public abstract class MySqlContainerTest {


    private static final Logger log = LoggerFactory.getLogger(MySqlContainerTest.class);

    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.36")
            .withLogConsumer(new Slf4jLogConsumer(log))
            .withDatabaseName("test-imstargg")
            .withUsername("root")
            .withPassword("1234")
            .withEnv("TZ", "UTC");
}
