package com.imstargg.batch.job.support;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class IdRangeJobParameter {

    private static final Logger log = LoggerFactory.getLogger(IdRangeJobParameter.class);

    public static final String ID_FROM_KEY = "id.from";
    public static final String ID_TO_KEY = "id.to";

    @Nullable
    private Long from;

    @Nullable
    private Long to;

    @PostConstruct
    void init() {
        log.debug("JobParameter[" + ID_FROM_KEY + "]={}", from);
        log.debug("JobParameter[" + ID_TO_KEY + "]={}", to);
    }

    @Value("#{jobParameters['" + ID_FROM_KEY + "']}")
    public void setFrom(@Nullable Long from) {
        this.from = from;
    }

    @Value("#{jobParameters['" + ID_TO_KEY + "']}")
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
