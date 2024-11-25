package com.imstargg.support.ratelimit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RateLimiterTest {

    private TestClock testClock;
    private RateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        testClock = new TestClock();
        rateLimiter = new RateLimiter(testClock, 10, 1, "test-limiter");
    }


    @Test
    void 제한_내의_요청은_허용된다() throws InterruptedException {
        // Given: Rate limit is 10 requests per second

        // When: Make 10 requests
        for (int i = 0; i < 10; i++) {
            rateLimiter.acquire();
        }

        // Then: Should not throw any exception
    }

    @Test
    void 시간_윈도우_이후_카운터가_리셋된다() throws InterruptedException {
        // Given: Make 10 requests
        for (int i = 0; i < 10; i++) {
            rateLimiter.acquire();
        }

        // When: Advance time by 1 second
        testClock.advanceMillis(1000);

        // Then: Should allow 10 more requests
        for (int i = 0; i < 10; i++) {
            rateLimiter.acquire();
        }
    }
}