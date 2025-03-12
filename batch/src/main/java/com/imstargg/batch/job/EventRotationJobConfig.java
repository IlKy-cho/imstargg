package com.imstargg.batch.job;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.BrawlStarsClientException;
import com.imstargg.client.brawlstars.response.ScheduledEventResponse;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleEventRotationCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleEventRotationCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleEventRotationItemCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleEventRotationItemCollectionJpaRepository;
import com.imstargg.support.alert.AlertCommand;
import com.imstargg.support.alert.AlertManager;
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

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
class EventRotationJobConfig {

    private static final Logger log = LoggerFactory.getLogger(EventRotationJobConfig.class);

    private static final String JOB_NAME = "eventRotationJob";
    private static final String STEP_NAME = "eventRotationStep";

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;
    private final BattleEventCollectionJpaRepository battleEventCollectionJpaRepository;
    private final BattleEventRotationCollectionJpaRepository battleEventRotationJpaRepository;
    private final BattleEventRotationItemCollectionJpaRepository battleEventRotationItemJpaRepository;

    EventRotationJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient,
            BattleEventCollectionJpaRepository battleEventCollectionJpaRepository,
            BattleEventRotationCollectionJpaRepository battleEventRotationJpaRepository,
            BattleEventRotationItemCollectionJpaRepository battleEventRotationItemJpaRepository
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.alertManager = alertManager;
        this.brawlStarsClient = brawlStarsClient;
        this.battleEventCollectionJpaRepository = battleEventCollectionJpaRepository;
        this.battleEventRotationJpaRepository = battleEventRotationJpaRepository;
        this.battleEventRotationItemJpaRepository = battleEventRotationItemJpaRepository;
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
        TaskletStepBuilder taskletStepBuilder = new TaskletStepBuilder(new StepBuilder(STEP_NAME, jobRepository));
        return taskletStepBuilder
                .tasklet((contribution, chunkContext) -> {

                    try {
                        BattleEventRotationCollectionEntity rotationEntity = new BattleEventRotationCollectionEntity();
                        List<BattleEventRotationItemCollectionEntity> rotationItemEntities = new ArrayList<>();
                        List<ScheduledEventResponse> eventResponseList = brawlStarsClient.getEventRotation();

                        eventResponseList.stream()
                                .map(response -> new BattleEventRotationItemCollectionEntity(
                                        rotationEntity,
                                        getBattleEventEntity(response),
                                        response.event().modifiers() == null ? List.of() : response.event().modifiers(),
                                        response.slotId(),
                                        response.startTime(),
                                        response.endTime()
                                )).forEach(rotationItemEntities::add);
                        battleEventRotationJpaRepository.save(rotationEntity);
                        battleEventRotationItemJpaRepository.saveAll(rotationItemEntities);
                    } catch (BrawlStarsClientException.InMaintenance e) {
                        log.info("Brawl Stars is in maintenance", e);
                    }

                    deleteOldRotation();
                    return RepeatStatus.FINISHED;
                }, txManager)
                .build();
    }

    private void deleteOldRotation() {
        OffsetDateTime aWeekAgo = OffsetDateTime.now(clock).minusWeeks(1);
        List<BattleEventRotationCollectionEntity> rotationEntitiesToDelete = battleEventRotationJpaRepository
                .findAll().stream()
                .filter(entity -> entity.getCreatedAt().isBefore(aWeekAgo))
                .toList();
        List<BattleEventRotationItemCollectionEntity> rotationItemsToDelete = battleEventRotationItemJpaRepository
                .findAllByRotationIn(rotationEntitiesToDelete);
        battleEventRotationJpaRepository.deleteAllInBatch(rotationEntitiesToDelete);
        battleEventRotationItemJpaRepository.deleteAllInBatch(rotationItemsToDelete);
    }

    private BattleEventCollectionEntity getBattleEventEntity(ScheduledEventResponse response) {
        return battleEventCollectionJpaRepository.findByBrawlStarsId(response.event().id())
                .orElseGet(() -> {
                    BattleEventCollectionEntity entity = battleEventCollectionJpaRepository.save(
                            new BattleEventCollectionEntity(
                                    response.event().id(),
                                    response.event().mode(),
                                    response.event().map()
                            )
                    );
                    alertManager.alert(AlertCommand.builder()
                            .title("[" + JOB_NAME + "] 존재하지 않는 이벤트 추가")
                            .content(String.format("""
                                    - 이벤트 ID: %d
                                    - 모드: %s
                                    - 맵: %s
                                    """, response.event().id(), response.event().mode(), response.event().map()))
                            .build());
                    return entity;
                });
    }
}
