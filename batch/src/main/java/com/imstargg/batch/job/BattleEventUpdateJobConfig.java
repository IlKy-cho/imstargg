package com.imstargg.batch.job;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.SoloRankSeasonCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.SoloRankSeasonCollectionJpaRepository;
import com.imstargg.storage.db.core.player.BattleEntity;
import com.imstargg.storage.db.core.player.BattleJpaRepository;
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

import java.time.Clock;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.imstargg.storage.db.core.player.QBattleCollectionEntity.battleCollectionEntity;


@Configuration
class BattleEventUpdateJobConfig {

    private static final Logger log = LoggerFactory.getLogger(BattleEventUpdateJobConfig.class);

    private static final String JOB_NAME = "battleEventUpdateJob";
    private static final String STEP_NAME = "battleEventUpdateStep";

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final BattleJpaRepository battleJpaRepository;
    private final BattleEventCollectionJpaRepository battleEventCollectionJpaRepository;
    private final SoloRankSeasonCollectionJpaRepository soloRankSeasonCollectionJpaRepository;

    BattleEventUpdateJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            BattleJpaRepository battleJpaRepository,
            BattleEventCollectionJpaRepository battleEventCollectionJpaRepository,
            SoloRankSeasonCollectionJpaRepository soloRankSeasonCollectionJpaRepository
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.battleJpaRepository = battleJpaRepository;
        this.battleEventCollectionJpaRepository = battleEventCollectionJpaRepository;
        this.soloRankSeasonCollectionJpaRepository = soloRankSeasonCollectionJpaRepository;
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
                                                battleEntity.getEvent().getMap()
                                        );
                                        battleEventCollectionJpaRepository.save(event);
                                    }
                            );

                    updateSoloRankedSeason();
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
                    BattleEventCollectionEntity entity = new BattleEventCollectionEntity(
                            battleEntity.getEvent().getBrawlStarsId(),
                            battleEntity.getEvent().getMode(),
                            battleEntity.getEvent().getMap()
                    );
                    alertManager.alert(AlertCommand.builder()
                            .title("[" + JOB_NAME + "] 존재하지 않는 이벤트 추가")
                            .content(String.format(
                                    """
                                             - 이벤트 ID: %d
                                             - 모드: %s
                                             - 맵: %s
                                            """,
                                    entity.getBrawlStarsId(),
                                    entity.getMode(),
                                    entity.getMapBrawlStarsName()
                            )).build());
                    return entity;
                });
    }

    private void updateSoloRankedSeason() {
        SoloRankSeasonCollectionEntity season = soloRankSeasonCollectionJpaRepository
                .findFirstByOrderByNumberDescMonthDesc()
                .orElseGet(() -> soloRankSeasonCollectionJpaRepository.save(
                        SoloRankSeasonCollectionEntity.createFirst()
                ));

        if (!season.isLast(clock)) {
            return;
        }

        SoloRankSeasonCollectionEntity next = season.next();
        alertManager.alert(
                AlertCommand.builder()
                        .title("[" + JOB_NAME + "] 경쟁전 시즌 지남")
                        .content(String.format(
                                """
                                경쟁전 시즌이 지났습니다.
                                새로운 시즌을 추가합니다.
                                지난 시즌: %s
                                신규 시즌: %s
                                신규 시즌 시작일: %s
                                신규 시즌 종료일: %s
                                """,
                                season.getNumber() + "-" + season.getMonth(),
                                next.getNumber() + "-" + next.getMonth(),
                                next.getStartAt(),
                                next.getEndAt()
                        ))
                        .build()
        );
    }

    private void alertNotExistsBattleEventMode() {
        List<String> notFoundModes = battleEventCollectionJpaRepository.findAll().stream()
                .map(BattleEventCollectionEntity::getMode)
                .filter(mode -> BattleEventMode.find(mode) == BattleEventMode.NOT_FOUND)
                .toList();
        if (notFoundModes.isEmpty()) {
            return;
        }
        alertManager.alert(
                AlertCommand.builder()
                        .title("[" + JOB_NAME + "] 존재하지 않는 모드 추가")
                        .content(notFoundModes.stream().map(mode -> "- " + mode).collect(Collectors.joining("\n")))
                        .build()
        );
    }

    private void alertNotExistsBattleType() {
        List<String> notFoundBattleTypes = JPAQueryFactoryUtils.getQueryFactory(emf)
                .select(battleCollectionEntity.event.brawlStarsId, battleCollectionEntity.type)
                .from(battleCollectionEntity)
                .groupBy(battleCollectionEntity.event.brawlStarsId, battleCollectionEntity.type)
                .fetch()
                .stream()
                .map(tuple -> tuple.get(battleCollectionEntity.type))
                .distinct()
                .filter(Objects::nonNull)
                .filter(battleType -> BattleType.find(battleType) == BattleType.NOT_FOUND)
                .toList();
        if (notFoundBattleTypes.isEmpty()) {
            return;
        }
        alertManager.alert(
                AlertCommand.builder()
                        .title("[" + JOB_NAME + "] 존재하지 않는 배틀 타입 추가")
                        .content(notFoundBattleTypes.stream().map(battleType -> "- " + battleType).collect(Collectors.joining("\n")))
                        .build()
        );
    }
}
