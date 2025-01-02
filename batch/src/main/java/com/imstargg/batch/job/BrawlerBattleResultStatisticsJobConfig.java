package com.imstargg.batch.job;

import com.imstargg.batch.domain.BattleResultStatisticsCollectedFilter;
import com.imstargg.batch.domain.BrawlerBattleResultStatisticsProcessorWithCache;
import com.imstargg.batch.domain.BrawlersBattleResultStatisticsProcessorWithCache;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.job.support.PeriodDateTimeJobParameter;
import com.imstargg.batch.job.support.querydsl.QuerydslPagingItemReader;
import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.imstargg.storage.db.core.QBattleCollectionEntity.battleCollectionEntity;

@Configuration
public class BrawlerBattleResultStatisticsJobConfig {

    private static final String JOB_NAME = "brawlerBattleResultStatisticsJob";
    private static final String STEP_NAME = "brawlerBattleResultStatisticsStep";
    private static final int CHUNK_SIZE = 100;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;

    public BrawlerBattleResultStatisticsJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .validator(new DefaultJobParametersValidator(new String[]{"date"}, new String[]{}))
                .build();
    }

    @Bean(JOB_NAME + "PeriodDateTimeJobParameter")
    @JobScope
    PeriodDateTimeJobParameter periodDateTimeJobParameter() {
        return new PeriodDateTimeJobParameter();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<BattleCollectionEntity, BattleCollectionEntity>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslPagingItemReader<BattleCollectionEntity> reader() {
        var reader = new QuerydslPagingItemReader<>(emf, CHUNK_SIZE, false, queryFactory -> queryFactory
                .selectFrom(battleCollectionEntity)
                .where(
                        battleCollectionEntity.createdAt.goe(periodDateTimeJobParameter().getFrom()),
                        battleCollectionEntity.createdAt.lt(periodDateTimeJobParameter().getTo())
                )
                .orderBy(battleCollectionEntity.createdAt.desc())
        );
        reader.setTransacted(false);
        reader.setSaveState(true);
        return reader;
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    ItemProcessor<BattleCollectionEntity, BattleCollectionEntity> processor() {
        return item -> {
            if (item.getResult() == null) {
                return null;
            }
            if (item.getEvent().getBrawlStarsId() == null || item.getEvent().getBrawlStarsId() == 0L) {
                return null;
            }
            if (!BattleType.statisticsCollected(item.getType())) {
                return null;
            }
            if (item.getResult() == null) {
                return null;
            }
            if (item.getTeams().size() != 2) {
                throw new IllegalStateException(
                        "Invalid teams size: " + item.getTeams().size() + ", battleId: " + item.getId());
            }

            return item;
        };
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    BrawlerResultStatisticsJobItemWriter writer() {
        return new BrawlerResultStatisticsJobItemWriter(
                emf,
                battleResultStatisticsCollectedFilter(),
                brawlerBattleResultStatisticsProcessor(),
                brawlersBattleResultStatisticsProcessor()
        );
    }

    @Bean
    @StepScope
    BattleResultStatisticsCollectedFilter battleResultStatisticsCollectedFilter() {
        return new BattleResultStatisticsCollectedFilter(emf);
    }

    @Bean
    @StepScope
    BrawlerBattleResultStatisticsProcessorWithCache brawlerBattleResultStatisticsProcessor() {
        return new BrawlerBattleResultStatisticsProcessorWithCache(emf);
    }

    @Bean
    @StepScope
    BrawlersBattleResultStatisticsProcessorWithCache brawlersBattleResultStatisticsProcessor() {
        return new BrawlersBattleResultStatisticsProcessorWithCache(emf);
    }

}
