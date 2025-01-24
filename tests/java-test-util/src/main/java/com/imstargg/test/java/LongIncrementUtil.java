package com.imstargg.test.java;

import java.util.concurrent.atomic.AtomicLong;

public abstract class LongIncrementUtil {

    private static final AtomicLong value = new AtomicLong(0);

    public static long next() {
        return value.incrementAndGet();
    }

    public static long next(int bound) {
        return value.incrementAndGet() % bound;
    }

    public static long next(int origin, int bound) {
        return value.incrementAndGet() % (bound - origin) + origin;
    }

    private LongIncrementUtil() {
        throw new UnsupportedOperationException();
    }
}
