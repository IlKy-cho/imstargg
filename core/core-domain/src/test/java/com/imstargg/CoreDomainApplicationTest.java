package com.imstargg;

import com.imstargg.core.domain.test.ContextTest;
import com.imstargg.core.domain.test.TestContextConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TestContextConfig.class)
class CoreDomainApplicationTest extends ContextTest {

    @Test
    void contextLoads() {
    }
}
