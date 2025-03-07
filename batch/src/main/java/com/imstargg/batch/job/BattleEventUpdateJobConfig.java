package com.imstargg.batch.job;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.BattleEntity;
import com.imstargg.storage.db.core.BattleJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionJpaRepository;
import com.imstargg.support.alert.AlertCommand;
import com.imstargg.support.alert.AlertManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.imstargg.storage.db.core.QBattleCollectionEntity.battleCollectionEntity;

@Configuration
class BattleEventUpdateJobConfig {

    private static final Logger log = LoggerFactory.getLogger(BattleEventUpdateJobConfig.class);

    private static final String JOB_NAME = "battleEventUpdateJob";
    private static final String STEP_NAME = "battleEventUpdateStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final BattleJpaRepository battleJpaRepository;
    private final BattleEventCollectionJpaRepository battleEventCollectionJpaRepository;

    BattleEventUpdateJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            BattleJpaRepository battleJpaRepository,
            BattleEventCollectionJpaRepository battleEventCollectionJpaRepository
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.battleJpaRepository = battleJpaRepository;
        this.battleEventCollectionJpaRepository = battleEventCollectionJpaRepository;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        TaskletStepBuilder taskletStepBuilder = new TaskletStepBuilder(new StepBuilder(STEP_NAME, jobRepository));
        return taskletStepBuilder
                .tasklet((contribution, chunkContext) -> {
                    List<Long> eventIds = battleJpaRepository
                            .findAllDistinctEventBrawlStarsIdsByBattleTypeInAndGreaterThanEqualBattleTime(
                                    null,
                                    null
                            );
                    log.debug("eventIds: {}", eventIds);
                    eventIds.stream()
                            .map(eventId -> battleJpaRepository.findLatestBattleByEventBrawlStarsIdAndBattleTypeIn(eventId, null))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .filter(battleEntity ->
                                    battleEntity.getEvent().getBrawlStarsId() != null
                                            && battleEntity.getEvent().getBrawlStarsId() > 0
                            )
                            .forEach(battleEntity -> {
                                        BattleEventCollectionEntity event = getBattleEventEntity(battleEntity);
                                        event.update(
                                                battleEntity.getEvent().getMode(),
                                                battleEntity.getEvent().getMap(),
                                                battleEntity.getMode(),
                                                battleEntity.getBattleTime()
                                        );
                                        battleEventCollectionJpaRepository.save(event);
                                    }
                            );

                    alertNotExistsBattleEventMode();
                    alertNotExistsBattleType();

                    return RepeatStatus.FINISHED;
                }, txManager).build();
    }

    private BattleEventCollectionEntity getBattleEventEntity(BattleEntity battleEntity) {
        return battleEventCollectionJpaRepository
                .findByBrawlStarsId(
                        Objects.requireNonNull(battleEntity.getEvent().getBrawlStarsId())
                ).orElseGet(() -> {
                    alertManager.alert(AlertCommand.builder()
                            .title("[" + JOB_NAME + "] 존재하지 않는 이벤트 추가")
                            .content(String.format(
                                    """
                                     - 이벤트 ID: %d
                                     - 모드: %s
                                     - 맵: %s
                                    """,
                                    battleEntity.getEvent().getBrawlStarsId(),
                                    battleEntity.getEvent().getMode(),
                                    battleEntity.getEvent().getMap())
                            ).build());
                    return new BattleEventCollectionEntity(
                            battleEntity.getEvent().getBrawlStarsId(),
                            battleEntity.getEvent().getMode(),
                            battleEntity.getEvent().getMap()
                    );
                });
    }

    private void alertNotExistsBattleEventMode() {
        battleEventCollectionJpaRepository.findAll().stream()
                .map(BattleEventCollectionEntity::getMode)
                .filter(mode -> BattleEventMode.find(mode) == BattleEventMode.NOT_FOUND)
                .forEach(mode -> alertManager.alert(AlertCommand.builder()
                        .title("[" + JOB_NAME + "] 존재하지 않는 모드 추가")
                        .content(String.format(
                                """
                                 - 모드: %s
                                """,
                                mode)
                        ).build()));
    }

    private void alertNotExistsBattleType() {
        JPAQueryFactoryUtils.getQueryFactory(emf)
                .select(battleCollectionEntity.event.brawlStarsId, battleCollectionEntity.type)
                .from(battleCollectionEntity)
                .groupBy(battleCollectionEntity.event.brawlStarsId, battleCollectionEntity.type)
                .fetch()
                .stream()
                .map(tuple -> tuple.get(battleCollectionEntity.type))
                .distinct()
                .filter(battleType -> BattleType.find(battleType) == BattleType.NOT_FOUND)
                .forEach(battleType -> alertManager.alert(AlertCommand.builder()
                        .title("[" + JOB_NAME + "] 존재하지 배틀 타입 추가")
                        .content(String.format(
                                """
                                 - 배틀 타입: %s
                                """,
                                battleType)
                        ).build()));

    }
}
