package com.imstargg.batch.job.statistics;

import com.imstargg.batch.domain.statistics.PlayerBrawlerStatisticsCollector;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.querydsl.QuerydslEntityCursorItemReader;
import com.imstargg.storage.db.core.player.PlayerBrawlerCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.imstargg.storage.db.core.player.QPlayerBrawlerCollectionEntity.playerBrawlerCollectionEntity;

@Configuration
public class PlayerBrawlerStatisticsJobConfig {

    private static final String JOB_NAME = "playerBrawlerStatisticsJob";
    private static final String STEP_NAME = "playerBrawlerStatisticsStep";
    private static final int CHUNK_SIZE = 10_000;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;
    private final EntityManager em;

    private final AlertManager alertManager;

    public PlayerBrawlerStatisticsJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            EntityManager em,
            AlertManager alertManager
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.em = em;
        this.alertManager = alertManager;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .incrementer(new RunIdIncrementer())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<PlayerBrawlerCollectionEntity, PlayerBrawlerCollectionEntity>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .writer(writer())
                .listener(new StepExecutionListener() {
                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        collector().save();
                        return null;
                    }
                })
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslEntityCursorItemReader<PlayerBrawlerCollectionEntity> reader() {
        var reader = new QuerydslEntityCursorItemReader<PlayerBrawlerCollectionEntity>(
                emf,
                CHUNK_SIZE,
                (lastEntity, queryFactory) -> queryFactory
                        .selectFrom(playerBrawlerCollectionEntity)
                        .where(
                                lastEntity == null ?
                                        null : playerBrawlerCollectionEntity.id.gt(lastEntity.getId())
                        )
                        .orderBy(playerBrawlerCollectionEntity.id.asc())
                        .limit(CHUNK_SIZE)
        );
        reader.setSaveState(false);
        reader.setTransacted(false);
        return reader;
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    ItemWriter<PlayerBrawlerCollectionEntity> writer() {
        var collector = collector();
        return chunk -> chunk.forEach(collector::collect);
    }

    @Bean
    @StepScope
    PlayerBrawlerStatisticsCollector collector() {
        return new PlayerBrawlerStatisticsCollector(em);
    }
}
