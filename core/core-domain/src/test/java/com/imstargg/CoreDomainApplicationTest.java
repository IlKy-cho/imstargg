package com.imstargg;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootApplication
@SpringBootTest
@Tag("context")
class CoreDomainApplicationTest {

    @Test
    void contextLoads() {
    }
}
