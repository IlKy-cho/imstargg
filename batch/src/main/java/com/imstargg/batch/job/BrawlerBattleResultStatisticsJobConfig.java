package com.imstargg.batch.job;

import com.imstargg.batch.domain.BrawlerBattleResultStatisticsCollector;
import com.imstargg.batch.domain.BrawlerEnemyBattleResultStatisticsCollector;
import com.imstargg.batch.domain.BrawlersBattleResultStatisticsCollector;
import com.imstargg.batch.job.support.DateJobParameter;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionJpaRepository;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Configuration
class BrawlerBattleResultStatisticsJobConfig {

    private static final Logger log = LoggerFactory.getLogger(BrawlerBattleResultStatisticsJobConfig.class);

    private static final String JOB_NAME = "brawlerBattleResultStatisticsJob";
    private static final String STEP_NAME = "brawlerBattleResultStatisticsStep";
    private static final int CHUNK_SIZE = 1000;

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final BattleCollectionJpaRepository battleCollectionJpaRepository;

    BrawlerBattleResultStatisticsJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            BattleCollectionJpaRepository battleCollectionJpaRepository
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.battleCollectionJpaRepository = battleCollectionJpaRepository;
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

    @Bean(JOB_NAME + "DateJobParameter")
    @JobScope
    DateJobParameter dateJobParameter() {
        return new DateJobParameter();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        TaskletStepBuilder taskletStepBuilder = new TaskletStepBuilder(new StepBuilder(STEP_NAME, jobRepository));
        return taskletStepBuilder
                .tasklet((contribution, chunkContext) -> {
                    boolean hasNext = true;
                    LocalDate date = Objects.requireNonNull(dateJobParameter().getDate());
                    OffsetDateTime fromDateTime = date.atStartOfDay().atZone(clock.getZone()).toOffsetDateTime();
                    OffsetDateTime toDateTime = date.plusDays(1).atStartOfDay().atZone(clock.getZone()).toOffsetDateTime();
                    PageRequest pageRequest = PageRequest.ofSize(CHUNK_SIZE);
                    while (hasNext) {
                        log.debug("Reading page: {}", pageRequest.getPageNumber());
                        Slice<BattleCollectionEntity> slice = battleCollectionJpaRepository
                                .findSliceWithPlayerByBattleTimeGreaterThanEqualAndBattleTimeLessThan(
                                        fromDateTime, toDateTime, pageRequest);
                        List<BattleCollectionEntity> battles = slice.getContent();

                        for (BattleCollectionEntity battle : battles) {
                            if (!validate(battle)) {
                                continue;
                            }
                            brawlerBattleResultStatisticsCollector().collect(battle);
                            brawlersBattleResultStatisticsCollector().collect(battle);
                            brawlerEnemyBattleResultStatisticsCollector().collect(battle);
                        }

                        hasNext = slice.hasNext();
                        pageRequest = pageRequest.next();
                    }

                    EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(emf);
                    if (em == null) {
                        throw new DataAccessResourceFailureException("Unable to obtain a transactional EntityManager");
                    }

                    brawlerBattleResultStatisticsCollector().result().forEach(em::persist);
                    brawlersBattleResultStatisticsCollector().result().forEach(em::persist);
                    brawlerEnemyBattleResultStatisticsCollector().result().forEach(em::persist);

                    return RepeatStatus.FINISHED;
                }, txManager).build();
    }

    private boolean validate(BattleCollectionEntity battle) {
        if (battle.getResult() == null) {
            return false;
        }
        if (!battle.existsEventId()) {
            return false;
        }
        if (!BattleType.find(battle.getType()).isRegular()) {
            return false;
        }
        if (battle.getTeams().size() != 2) {
            throw new IllegalStateException(
                    "Invalid teams size: " + battle.getTeams().size() + ", battleId: " + battle.getId());
        }
        return true;
    }


    @Bean
    @StepScope
    BrawlerBattleResultStatisticsCollector brawlerBattleResultStatisticsCollector() {
        return new BrawlerBattleResultStatisticsCollector();
    }

    @Bean
    @StepScope
    BrawlersBattleResultStatisticsCollector brawlersBattleResultStatisticsCollector() {
        return new BrawlersBattleResultStatisticsCollector();
    }

    @Bean
    @StepScope
    BrawlerEnemyBattleResultStatisticsCollector brawlerEnemyBattleResultStatisticsCollector() {
        return new BrawlerEnemyBattleResultStatisticsCollector();
    }

}
