package com.imstargg.test.java;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class IntegerIncrementUtil {

    private static final AtomicInteger value = new AtomicInteger(0);

    public static int next() {
        return value.incrementAndGet();
    }

    public static int next(int bound) {
        return value.incrementAndGet() % bound;
    }

    public static int next(int origin, int bound) {
        return value.incrementAndGet() % (bound - origin) + origin;
    }

    public static <T> T next(T[] values) {
        return values[next(values.length)];
    }

    private IntegerIncrementUtil() {
        throw new UnsupportedOperationException();
    }
}
