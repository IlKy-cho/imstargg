package com.imstargg.support.ratelimit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Tag("context")
class RateLimiterContextTest {

    private TestClock testClock;
    private RateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        testClock = new TestClock();
        rateLimiter = new RateLimiter(testClock, 10, 1, "test-limiter");
    }


    @Test
    void 제한_초과_요청은_락걸린다() throws InterruptedException {
        // Given: Rate limit is 10 requests per second
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger acquiredCount = new AtomicInteger(0);

        // When: Try to make 15 requests immediately
        Thread thread = new Thread(() -> {
            try {
                for (int i = 0; i < 15; i++) {
                    rateLimiter.acquire();
                    acquiredCount.incrementAndGet();
                }
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.start();
        Thread.sleep(500); // Give some time for the requests to be processed

        // Then: Only 10 requests should be processed immediately
        Assertions.assertThat(acquiredCount.get()).isEqualTo(10);

        // When: Advance time by 1 second
        testClock.advanceMillis(1000);
        latch.await();

        // Then: All 15 requests should eventually be processed
        Assertions.assertThat(acquiredCount.get()).isEqualTo(15);
    }

}