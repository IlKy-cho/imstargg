package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerTagFilter;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.IdRangeJobParameter;
import com.imstargg.batch.job.support.JpaItemListWriter;
import com.imstargg.batch.job.support.querydsl.QuerydslPagingItemReader;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.PlayerCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

import static com.imstargg.storage.db.core.QBattleCollectionEntity.battleCollectionEntity;

@Configuration
public class NewPlayerJobConfig {

    private static final String JOB_NAME = "newPlayerJob";
    private static final String STEP_NAME = "newPlayerStep";
    private static final int CHUNK_SIZE = 10;


    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;
    private final TaskExecutor taskExecutor;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;

    NewPlayerJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            TaskExecutor taskExecutor,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.taskExecutor = taskExecutor;
        this.alertManager = alertManager;
        this.brawlStarsClient = brawlStarsClient;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .validator(new DefaultJobParametersValidator(new String[]{}, new String[]{"id.from", "id.to"}))
                .build();
    }

    @Bean(JOB_NAME + "IdRangeJobParameter")
    @JobScope
    IdRangeJobParameter<Long> idRangeJobParameter() {
        return new IdRangeJobParameter<>();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<BattleCollectionEntity, List<PlayerCollectionEntity>>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .writer(writer())
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslPagingItemReader<BattleCollectionEntity> reader() {
        return new QuerydslPagingItemReader<>(emf, CHUNK_SIZE, false, queryFactory -> queryFactory
                .selectFrom(battleCollectionEntity)
                .where(idGoe(), idLt())
                .orderBy(battleCollectionEntity.id.asc())
        );
    }

    private BooleanExpression idGoe() {
        if (idRangeJobParameter().getFrom() == null) {
            return null;
        }
        return battleCollectionEntity.id.goe(idRangeJobParameter().getFrom());
    }

    private BooleanExpression idLt() {
        if (idRangeJobParameter().getTo() == null) {
            return null;
        }
        return battleCollectionEntity.id.lt(idRangeJobParameter().getTo());
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    NewPlayerJobItemProcessor processor() {
        return new NewPlayerJobItemProcessor(playerTagFilter(), brawlStarsClient);
    }

    @Bean(STEP_NAME + "PlayerTagFilter")
    @StepScope
    PlayerTagFilter playerTagFilter() {
        return new PlayerTagFilter(emf);
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    JpaItemListWriter<PlayerCollectionEntity> writer() {
        return new JpaItemListWriter<>(
                new JpaItemWriterBuilder<PlayerCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build()
        );
    }
}
