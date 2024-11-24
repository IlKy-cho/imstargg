package com.imstargg.support.ratelimit;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class TestClock extends Clock {

    private Instant now;

    public TestClock() {
        this.now = Instant.now();
    }

    @Override
    public ZoneId getZone() {
        return ZoneId.systemDefault();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return this;
    }

    @Override
    public Instant instant() {
        return now;
    }

    @Override
    public long millis() {
        return now.toEpochMilli();
    }

    public void advanceMillis(long millis) {
        now = now.plusMillis(millis);
    }
}
