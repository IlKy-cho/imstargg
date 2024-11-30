package com.imstargg.batch.job.support;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public class PeriodDateTimeJobParameter {

    @Nullable
    private LocalDateTime from;

    @Nullable
    private LocalDateTime to;

    @Value("#{jobParameters['period.from']}")
    public void setFrom(@Nullable LocalDateTime from) {
        this.from = from;
    }

    @Value("#{jobParameters['period.to']}")
    public void setTo(@Nullable LocalDateTime to) {
        this.to = to;
    }

    @Nullable
    public LocalDateTime getFrom() {
        return from;
    }

    @Nullable
    public LocalDateTime getTo() {
        return to;
    }
}
