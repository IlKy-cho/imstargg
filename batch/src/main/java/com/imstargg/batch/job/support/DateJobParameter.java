package com.imstargg.batch.job.support;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public class DateJobParameter {

    private static final Logger log = LoggerFactory.getLogger(DateJobParameter.class);

    public static final String KEY = "date";

    @Nullable
    private LocalDate date;

    @Value("#{jobParameters['" + KEY + "']}")
    public void setDate(@Nullable LocalDate date) {
        this.date = date;
    }

    @PostConstruct
    void init() {
        log.debug("JobParameter[" + KEY + "]={}", date);
    }

    @Nullable
    public LocalDate getDate() {
        return date;
    }
}
