package com.imstargg.batch.job.support;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class IdRangeJobParameter {

    private static final Logger log = LoggerFactory.getLogger(IdRangeJobParameter.class);

    @Nullable
    private Long from;

    @Nullable
    private Long to;

    @PostConstruct
    void init() {
        log.debug("JobParameter[id.from]={}", from);
        log.debug("JobParameter[id.to]={}", to);
    }

    @Value("#{jobParameters['id.from']}")
    public void setFrom(@Nullable Long from) {
        this.from = from;
    }

    @Value("#{jobParameters['id.to']}")
    public void setTo(@Nullable Long to) {
        this.to = to;
    }

    @Nullable
    public Long getFrom() {
        return from;
    }

    @Nullable
    public Long getTo() {
        return to;
    }
}
