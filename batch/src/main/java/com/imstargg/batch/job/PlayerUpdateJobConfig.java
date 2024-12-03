package com.imstargg.batch.job;

import com.imstargg.batch.job.support.ChunkSizeJobParameter;
import com.imstargg.batch.job.support.ExceptionLoggingJobExecutionListener;
import com.imstargg.batch.job.support.RunTimestampIncrementer;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.core.enums.PlayerStatus;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionJpaRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Limit;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class PlayerUpdateJobConfig {


    private static final String JOB_NAME = "playerUpdateJob";
    private static final String STEP_NAME = "playerUpdateStep";

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    private final BrawlStarsClient brawlStarsClient;
    private final PlayerCollectionJpaRepository playerRepository;

    PlayerUpdateJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            BrawlStarsClient brawlStarsClient,
            PlayerCollectionJpaRepository playerRepository
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.brawlStarsClient = brawlStarsClient;
        this.playerRepository = playerRepository;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .incrementer(new RunTimestampIncrementer(clock))
                .start(step())
                .listener(new ExceptionLoggingJobExecutionListener())
                .build();
    }

    @Bean(JOB_NAME + "ChunkSizeJobParameter")
    @JobScope
    ChunkSizeJobParameter chunkSizeJobParameter() {
        return new ChunkSizeJobParameter(10);
    }

    @Bean(STEP_NAME)
    @JobScope
    public Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        TaskletStepBuilder taskletStepBuilder = new TaskletStepBuilder(stepBuilder);
        return taskletStepBuilder
                .tasklet((contribution, chunkContext) -> {
                    List<PlayerCollectionEntity> items = read();

                    List<PlayerCollectionEntity> results = new ArrayList<>();
                    for (PlayerCollectionEntity item : items) {
                        PlayerCollectionEntity processed = processor().process(item);
                        if (processed != null) {
                            results.add(processed);
                        }
                    }

                    if (!results.isEmpty()) {
                        playerRepository.saveAll(results);
                    }

                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    private List<PlayerCollectionEntity> read() {
        return playerRepository
                .findAllByDeletedFalseAndStatusInOrderByUpdateWeight(
                        List.of(PlayerStatus.BATTLE_UPDATED),
                        Limit.of(chunkSizeJobParameter().getSize())
                );
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    PlayerUpdateJobItemProcessor processor() {
        return new PlayerUpdateJobItemProcessor(clock, brawlStarsClient);
    }

}
