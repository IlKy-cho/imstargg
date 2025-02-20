package com.imstargg.batch.job.support;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class JobParametersInjectionIncrementer implements JobParametersIncrementer {

    private final Map<String, LocalDate> localDateParameters = new HashMap<>();
    private final Map<String, Long> longParameters = new HashMap<>();

    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters params = (parameters == null) ? new JobParameters() : parameters;
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(params);
        localDateParameters.entrySet().stream()
                .filter(entry -> !params.getParameters().containsKey(entry.getKey()))
                .forEach(entry -> jobParametersBuilder.addLocalDate(entry.getKey(), entry.getValue()));
        longParameters.entrySet().stream()
                .filter(entry -> !params.getParameters().containsKey(entry.getKey()))
                .forEach(entry -> jobParametersBuilder.addLong(entry.getKey(), entry.getValue()));
        return jobParametersBuilder.toJobParameters();
    }

    public JobParametersInjectionIncrementer addLocalDate(String key, LocalDate value) {
        localDateParameters.put(key, value);
        return this;
    }

    public JobParametersInjectionIncrementer addLong(String key, Long value) {
        longParameters.put(key, value);
        return this;
    }
}
