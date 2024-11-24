package com.imstargg.support.ratelimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RateLimiter {

    private static final Logger log = LoggerFactory.getLogger(RateLimiter.class);

    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicBoolean exceedLogged = new AtomicBoolean(false);
    private final AtomicBoolean warningLogged = new AtomicBoolean(false);
    private final Lock lock = new ReentrantLock();
    private final Clock clock;
    private final int limit;
    private final int timeWindow;
    private final String name;
    private long lastResetTime;

    public RateLimiter(Clock clock, int limit, int timeWindow, String name) {
        this.clock = clock;
        this.limit = limit;
        this.timeWindow = timeWindow;
        this.name = name;
        this.lastResetTime = clock.millis();
    }

    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            while (!tryAcquire()) {
                Thread.sleep(100);
            }
        } finally {
            lock.unlock();
        }
    }

    private boolean tryAcquire() {
        long currentTime = clock.millis();
        if (currentTime - lastResetTime >= timeWindow * 1000L) {
            counter.set(0);
            exceedLogged.set(false);
            warningLogged.set(false);
            lastResetTime = currentTime;
        }

        int count = counter.incrementAndGet();
        if (count == limit * 0.8 && !warningLogged.getAndSet(true)) {
            log.warn("[RateLimit] 80% 사용량 도달: {} ({} / {})", name, count, limit);
        } else if (count > limit && !exceedLogged.getAndSet(true)) {
            log.warn("[RateLimit] 제한 초과: {} ({} / {})", name, count, limit);
        }
        return count <= limit;
    }
}
