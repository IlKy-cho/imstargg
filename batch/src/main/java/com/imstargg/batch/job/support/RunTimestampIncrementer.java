package com.imstargg.batch.job.support;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.time.Clock;

public class RunTimestampIncrementer implements JobParametersIncrementer {

    private static final String RUN_TIMESTAMP_KEY = "run.timestamp";

    private final Clock clock;

    public RunTimestampIncrementer(Clock clock) {
        this.clock = clock;
    }

    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters params = (parameters == null) ? new JobParameters() : parameters;

        return new JobParametersBuilder(params)
                .addLong(RUN_TIMESTAMP_KEY, clock.millis())
                .toJobParameters();
    }
}
