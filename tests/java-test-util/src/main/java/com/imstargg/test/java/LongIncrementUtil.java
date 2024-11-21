package com.imstargg.test.java;

import java.util.concurrent.atomic.AtomicLong;

public abstract class LongIncrementUtil {

    private static AtomicLong value = new AtomicLong(0);

    public static long next() {
        return value.incrementAndGet();
    }

    private LongIncrementUtil() {
        throw new UnsupportedOperationException();
    }
}
