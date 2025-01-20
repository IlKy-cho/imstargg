package com.imstargg.batch.job.support;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateIncrementer implements JobParametersIncrementer {

    private static final String KEY = "date";

    private final Clock clock;

    public DateIncrementer(Clock clock) {
        this.clock = clock;
    }

    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters params = (parameters == null) ? new JobParameters() : parameters;
        JobParameter<?> dateParameter = params.getParameters().get(KEY);
        LocalDate date = LocalDate.now(clock).minusDays(1);
        if (dateParameter != null) {
            try {
                LocalDate prevDate = LocalDate.parse(dateParameter.getValue().toString());
                LocalDate nextDate = prevDate.plusDays(1);
                date = date.isBefore(nextDate) ? date : nextDate;
            } catch (DateTimeParseException exception) {
                throw new IllegalArgumentException("Invalid value for parameter " + KEY, exception);
            }
        }
        return new JobParametersBuilder(params)
                .addLocalDate(KEY, date)
                .toJobParameters();
    }
}
