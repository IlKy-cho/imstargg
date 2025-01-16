package com.imstargg.batch.job.support;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;

public class IdRangeJobParameter<T> {

    @Nullable
    private T from;

    @Nullable
    private T to;

    @Value("#{jobParameters['id.from']}")
    public void setFrom(@Nullable T from) {
        this.from = from;
    }

    @Value("#{jobParameters['id.to']}")
    public void setTo(@Nullable T to) {
        this.to = to;
    }

    @Nullable
    public T getFrom() {
        return from;
    }

    @Nullable
    public T getTo() {
        return to;
    }
}
