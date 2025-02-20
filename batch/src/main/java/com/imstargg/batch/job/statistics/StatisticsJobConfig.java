package com.imstargg.batch.job.statistics;

import com.imstargg.batch.job.support.DateJobParameter;
import com.imstargg.batch.job.support.JobParametersInjectionIncrementer;
import com.imstargg.batch.job.support.RunTimestampIncrementer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.LocalDate;

@Configuration
class StatisticsJobConfig {

    private static final String JOB_NAME = "statisticsJob";

    private static final String TODAY_JOB_NAME = "todayStatisticsJob";
    private static final String ONE_DAY_AGO_JOB_NAME = "oneDayAgoStatisticsJob";
    private static final String TWO_DAYS_AGO_JOB_NAME = "twoDaysAgoStatisticsJob";
    private static final String THREE_DAYS_AGO_JOB_NAME = "threeDaysAgoStatisticsJob";

    private final Clock clock;
    private final JobRepository jobRepository;

    private static final String RUN_TIMESTAMP = "run.timestamp";

    StatisticsJobConfig(Clock clock, JobRepository jobRepository) {
        this.clock = clock;
        this.jobRepository = jobRepository;
    }

    @Bean(TODAY_JOB_NAME)
    Job todayJob() {
        return new JobBuilder(TODAY_JOB_NAME, jobRepository)
                .incrementer(
                        new JobParametersInjectionIncrementer()
                                .addLocalDate(DateJobParameter.KEY, LocalDate.now(clock))
                                .addLong(RUN_TIMESTAMP, clock.millis())
                )
                .start(
                        new StepBuilder(TODAY_JOB_NAME + "Step", jobRepository)
                                .job(job())
                                .build()
                )
                .build();
    }

    @Bean(ONE_DAY_AGO_JOB_NAME)
    Job oneDayAgoJob() {
        return new JobBuilder(ONE_DAY_AGO_JOB_NAME, jobRepository)
                .incrementer(
                        new JobParametersInjectionIncrementer()
                                .addLocalDate(DateJobParameter.KEY, LocalDate.now(clock).minusDays(1))
                                .addLong(RUN_TIMESTAMP, clock.millis())
                )
                .start(
                        new StepBuilder(ONE_DAY_AGO_JOB_NAME + "Step", jobRepository)
                                .job(job())
                                .build()
                )
                .build();
    }

    @Bean(TWO_DAYS_AGO_JOB_NAME)
    Job twoDaysAgoJob() {
        return new JobBuilder(TWO_DAYS_AGO_JOB_NAME, jobRepository)
                .incrementer(
                        new JobParametersInjectionIncrementer()
                                .addLocalDate(DateJobParameter.KEY, LocalDate.now(clock).minusDays(2))
                                .addLong(RUN_TIMESTAMP, clock.millis())
                )
                .start(
                        new StepBuilder(TWO_DAYS_AGO_JOB_NAME + "Step", jobRepository)
                                .job(job())
                                .build()
                )
                .build();
    }

    @Bean(THREE_DAYS_AGO_JOB_NAME)
    Job threeDaysAgoJob() {
        return new JobBuilder(THREE_DAYS_AGO_JOB_NAME, jobRepository)
                .incrementer(
                        new JobParametersInjectionIncrementer()
                                .addLocalDate(DateJobParameter.KEY, LocalDate.now(clock).minusDays(3))
                                .addLong(RUN_TIMESTAMP, clock.millis())
                )
                .start(
                        new StepBuilder(THREE_DAYS_AGO_JOB_NAME + "Step", jobRepository)
                                .job(job())
                                .build()
                )
                .build();
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .incrementer(new RunTimestampIncrementer(clock))
                .start(brawlerBattleRankStatisticsJobStep(null))
                .next(brawlersBattleRankStatisticsJobStep(null))
                .next(brawlerBattleResultStatisticsJobStep(null))
                .next(brawlerEnemyBattleResultStatisticsJobStep(null))
                .next(brawlersBattleResultStatisticsJobStep(null))
                .build();
    }

    @Bean(JOB_NAME + "BrawlerBattleRankStatisticsJobStep")
    @JobScope
    Step brawlerBattleRankStatisticsJobStep(Job brawlerBattleRankStatisticsJob) {
        StepBuilder stepBuilder = new StepBuilder("brawlerBattleRankStatisticsJobStep", jobRepository);
        return stepBuilder
                .job(brawlerBattleRankStatisticsJob)
                .build();
    }

    @Bean(JOB_NAME + "BrawlersBattleRankStatisticsJobStep")
    @JobScope
    Step brawlersBattleRankStatisticsJobStep(Job brawlersBattleRankStatisticsJob) {
        StepBuilder stepBuilder = new StepBuilder("brawlersBattleRankStatisticsJobStep", jobRepository);
        return stepBuilder
                .job(brawlersBattleRankStatisticsJob)
                .build();
    }

    @Bean(JOB_NAME + "BrawlerBattleResultStatisticsJobStep")
    @JobScope
    Step brawlerBattleResultStatisticsJobStep(Job brawlerBattleResultStatisticsJob) {
        StepBuilder stepBuilder = new StepBuilder("brawlerBattleResultStatisticsJobStep", jobRepository);
        return stepBuilder
                .job(brawlerBattleResultStatisticsJob)
                .build();
    }

    @Bean(JOB_NAME + "BrawlerEnemyBattleResultStatisticsJobStep")
    @JobScope
    Step brawlerEnemyBattleResultStatisticsJobStep(Job brawlerEnemyBattleResultStatisticsJob) {
        StepBuilder stepBuilder = new StepBuilder("brawlerEnemyBattleResultStatisticsJobStep", jobRepository);
        return stepBuilder
                .job(brawlerEnemyBattleResultStatisticsJob)
                .build();
    }

    @Bean(JOB_NAME + "BrawlersBattleResultStatisticsJobStep")
    @JobScope
    Step brawlersBattleResultStatisticsJobStep(Job brawlersBattleResultStatisticsJob) {
        StepBuilder stepBuilder = new StepBuilder("brawlersBattleResultStatisticsJobStep", jobRepository);
        return stepBuilder
                .job(brawlersBattleResultStatisticsJob)
                .build();
    }
}
