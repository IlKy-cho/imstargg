package com.imstargg.support.ratelimit;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("develop")
class RateLimiterDevTest {

    @Test
    void 동시_요청을_정상적으로_처리한다() throws InterruptedException {
        // Given

        Clock clock = Clock.systemDefaultZone();
        RateLimiter rateLimiter = new RateLimiter(clock, 10, 1, "test-limiter");
        int timeWindowMillis = 1000; // 1초
        int limitPerWindow = 10;     // 초당 10개 요청 제한
        int threadCount = 20;
        int requestsPerThread = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        // 각 시간 윈도우별 성공한 요청 수를 기록
        Map<Long, AtomicInteger> requestsByWindow = new ConcurrentHashMap<>();

        // When: Multiple threads try to acquire permits simultaneously
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    for (int j = 0; j < requestsPerThread; j++) {
                        rateLimiter.acquire();

                        // 현재 시간 윈도우 계산 (1초 단위)
                        long currentWindow = clock.millis() / timeWindowMillis;
                        requestsByWindow
                                .computeIfAbsent(currentWindow, k -> new AtomicInteger())
                                .incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // Then: Wait for all threads to complete and verify the total requests
        latch.await();
        executorService.shutdown();
        assertThat(executorService.awaitTermination(10, TimeUnit.SECONDS)).isTrue();

        // 검증 1: 각 시간 윈도우별로 제한을 초과하지 않았는지 확인
        requestsByWindow.forEach((window, count) -> {
            assertThat(count.get())
                    .describedAs("시간 윈도우 %d에서 제한을 초과함: %d > %d", window, count.get(), limitPerWindow)
                    .isLessThanOrEqualTo(limitPerWindow);
        });

        // 검증 2: 모든 요청이 처리되었는지 확인
        int totalProcessed = requestsByWindow.values().stream()
                .mapToInt(AtomicInteger::get)
                .sum();
        assertThat(totalProcessed)
                .describedAs("모든 요청이 처리되지 않음")
                .isEqualTo(threadCount * requestsPerThread);

        // 검증 3: 시간 윈도우의 개수가 예상과 일치하는지 확인
        int expectedWindows = (int) Math.ceil((threadCount * requestsPerThread) / (double) limitPerWindow);
        assertThat(requestsByWindow)
                .describedAs("예상한 시간 윈도우 개수와 다름. 예상: %d, 실제: %d",
                        expectedWindows, requestsByWindow.size())
                .hasSize(expectedWindows);

        // 로깅: 각 시간 윈도우별 처리된 요청 수
        requestsByWindow.forEach((window, count) -> {
            System.out.printf("Window %d: processed %d requests%n",
                    window, count.get());
        });
    }

}