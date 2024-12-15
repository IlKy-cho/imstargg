package com.imstargg.batch.job.support;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public class PeriodDateTimeJobParameter {

    private static final Logger log = LoggerFactory.getLogger(PeriodDateTimeJobParameter.class);

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

    @PostConstruct
    void init() {
        log.debug("JobParameter[period.from]={}", from);
        log.debug("JobParameter[period.to]={}", to);
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
