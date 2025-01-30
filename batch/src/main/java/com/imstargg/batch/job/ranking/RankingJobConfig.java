package com.imstargg.batch.job.ranking;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RankingJobConfig {

    private static final String JOB_NAME = "rankingJob";

    private final JobRepository jobRepository;

    public RankingJobConfig(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean(JOB_NAME)
    public Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .incrementer(new RunIdIncrementer())
                .start(playerRankingJobStep(null))
                .next(brawlerRankingJobStep(null))
                .build();
    }

    @Bean(JOB_NAME + "PlayerRankingJobStep")
    @JobScope
    public Step playerRankingJobStep(Job playerRankingJob) {
        StepBuilder stepBuilder = new StepBuilder("playerRankingJob", jobRepository);
        return stepBuilder
                .job(playerRankingJob)
                .build();
    }

    @Bean(JOB_NAME + "BrawlerRankingJobStep")
    @JobScope
    public Step brawlerRankingJobStep(Job brawlerRankingJob) {
        StepBuilder stepBuilder = new StepBuilder("brawlerRankingJob", jobRepository);
        return stepBuilder
                .job(brawlerRankingJob)
                .build();
    }
}
