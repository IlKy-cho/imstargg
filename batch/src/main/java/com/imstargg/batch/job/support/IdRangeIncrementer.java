package com.imstargg.batch.job.support;


import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.util.function.LongSupplier;

public class IdRangeIncrementer implements JobParametersIncrementer {

    private final long size;
    private final LongSupplier maxIdSupplier;

    public IdRangeIncrementer(LongSupplier maxIdSupplier) {
        this(Long.MAX_VALUE, maxIdSupplier);
    }

    public IdRangeIncrementer(long size, LongSupplier maxIdSupplier) {
        this.size = size;
        this.maxIdSupplier = maxIdSupplier;
    }

    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters params = (parameters == null) ? new JobParameters() : parameters;
        JobParameter<?> idToParameter = params.getParameters().get(IdRangeJobParameter.ID_TO_KEY);
        long idFrom = 1;
        if (idToParameter != null) {
            try {
                idFrom = Long.parseLong(idToParameter.getValue().toString()) + 1;
            } catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Invalid value for parameter " +
                        IdRangeJobParameter.ID_TO_KEY, exception);
            }
        }
        long idTo = Math.min(idFrom + size - 1, maxIdSupplier.getAsLong());
        return new JobParametersBuilder(params)
                .addLong(IdRangeJobParameter.ID_FROM_KEY, idFrom)
                .addLong(IdRangeJobParameter.ID_TO_KEY, idTo)
                .toJobParameters();
    }
}
