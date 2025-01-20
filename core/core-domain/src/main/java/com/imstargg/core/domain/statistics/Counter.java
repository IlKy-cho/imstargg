package com.imstargg.core.domain.statistics;

public class Counter {

    private long count = 0;

    public void add(long count) {
        this.count += count;
    }

    public long getCount() {
        return count;
    }
}
