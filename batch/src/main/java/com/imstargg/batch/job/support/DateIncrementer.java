package com.imstargg.batch.job.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateIncrementer implements JobParametersIncrementer {

    private static final Logger log = LoggerFactory.getLogger(DateIncrementer.class);

    private final LocalDate maxDate;

    public DateIncrementer(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters params = (parameters == null) ? new JobParameters() : parameters;
        JobParameter<?> dateParameter = params.getParameters().get(DateJobParameter.KEY);
        LocalDate date = maxDate;
        if (dateParameter != null) {
            try {
                LocalDate prevDate = LocalDate.parse(dateParameter.getValue().toString());
                LocalDate nextDate = prevDate.plusDays(1);
                date = date.isBefore(nextDate) ? date : nextDate;
            } catch (DateTimeParseException exception) {
                throw new IllegalArgumentException("Invalid value for parameter " + DateJobParameter.KEY, exception);
            }
        }

        return new JobParametersBuilder(params)
                .addLocalDate(DateJobParameter.KEY, date)
                .toJobParameters();
    }
}
