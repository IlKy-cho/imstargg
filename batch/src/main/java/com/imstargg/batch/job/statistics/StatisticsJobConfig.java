package com.imstargg.batch.job.statistics;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StatisticsJobConfig {

    private static final String JOB_NAME = "statisticsJob";

    private final JobRepository jobRepository;

    public StatisticsJobConfig(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean(JOB_NAME)
    public Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(brawlerBattleRankStatisticsJobStep(null))
                .next(brawlersBattleRankStatisticsJobStep(null))
                .next(brawlerBattleResultStatisticsJobStep(null))
                .next(brawlerEnemyBattleResultStatisticsJobStep(null))
                .next(brawlersBattleResultStatisticsJobStep(null))
                .build();
    }

    @Bean(JOB_NAME + "BrawlerBattleRankStatisticsJobStep")
    @JobScope
    public Step brawlerBattleRankStatisticsJobStep(Job brawlerBattleRankStatisticsJob) {
        StepBuilder stepBuilder = new StepBuilder("brawlerBattleRankStatisticsJobStep", jobRepository);
        return stepBuilder
                .job(brawlerBattleRankStatisticsJob)
                .build();
    }

    @Bean(JOB_NAME + "BrawlersBattleRankStatisticsJobStep")
    @JobScope
    public Step brawlersBattleRankStatisticsJobStep(Job brawlersBattleRankStatisticsJob) {
        StepBuilder stepBuilder = new StepBuilder("brawlersBattleRankStatisticsJobStep", jobRepository);
        return stepBuilder
                .job(brawlersBattleRankStatisticsJob)
                .build();
    }

    @Bean(JOB_NAME + "BrawlerBattleResultStatisticsJobStep")
    @JobScope
    public Step brawlerBattleResultStatisticsJobStep(Job brawlerBattleResultStatisticsJob) {
        StepBuilder stepBuilder = new StepBuilder("brawlerBattleResultStatisticsJobStep", jobRepository);
        return stepBuilder
                .job(brawlerBattleResultStatisticsJob)
                .build();
    }

    @Bean(JOB_NAME + "BrawlerEnemyBattleResultStatisticsJobStep")
    @JobScope
    public Step brawlerEnemyBattleResultStatisticsJobStep(Job brawlerEnemyBattleResultStatisticsJob) {
        StepBuilder stepBuilder = new StepBuilder("brawlerEnemyBattleResultStatisticsJobStep", jobRepository);
        return stepBuilder
                .job(brawlerEnemyBattleResultStatisticsJob)
                .build();
    }

    @Bean(JOB_NAME + "BrawlersBattleResultStatisticsJobStep")
    @JobScope
    public Step brawlersBattleResultStatisticsJobStep(Job brawlersBattleResultStatisticsJob) {
        StepBuilder stepBuilder = new StepBuilder("brawlersBattleResultStatisticsJobStep", jobRepository);
        return stepBuilder
                .job(brawlersBattleResultStatisticsJob)
                .build();
    }
}
