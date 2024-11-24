package com.imstargg.support.ratelimit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    void 동시_요청을_정상적으로_처리한다() throws InterruptedException {
        // Given
        int threadCount = 20;
        int requestsPerThread = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successfulRequests = new AtomicInteger(0);

        // When: Multiple threads try to acquire permits simultaneously
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        rateLimiter.acquire();
                        successfulRequests.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        // Then: Wait for all threads to complete and verify the total requests
        latch.await();
        executorService.shutdown();

        // 총 요청 수는 시간 경과에 따라 허용된 요청 수와 같아야 함
        Assertions.assertThat(successfulRequests.get()).isEqualTo(threadCount * requestsPerThread);
    }
}