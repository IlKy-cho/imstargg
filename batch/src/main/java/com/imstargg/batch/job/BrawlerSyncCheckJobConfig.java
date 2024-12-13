package com.imstargg.batch.job;

import com.imstargg.batch.job.support.RunTimestampIncrementer;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.storage.db.core.brawlstars.BrawlerCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.GadgetCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.StarPowerCollectionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.Clock;
import java.util.List;

import static com.imstargg.storage.db.core.brawlstars.QBrawlerCollectionEntity.brawlerCollectionEntity;
import static com.imstargg.storage.db.core.brawlstars.QGadgetCollectionEntity.gadgetCollectionEntity;
import static com.imstargg.storage.db.core.brawlstars.QStarPowerCollectionEntity.starPowerCollectionEntity;

@Configuration
public class BrawlerSyncCheckJobConfig {

    private static final Logger log = LoggerFactory.getLogger(BrawlerSyncCheckJobConfig.class);

    private static final String JOB_NAME = "brawlerSyncCheckJob";
    private static final String STEP_NAME = "brawlerSyncCheckStep";

    private final Clock clock;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;
    private final BrawlStarsClient brawlStarsClient;

    public BrawlerSyncCheckJobConfig(
            Clock clock,
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            BrawlStarsClient brawlStarsClient
    ) {
        this.clock = clock;
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.brawlStarsClient = brawlStarsClient;
    }

    @Bean(JOB_NAME)
    public Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .incrementer(new RunTimestampIncrementer(clock))
                .start(step())
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    public Step step() {
        TaskletStepBuilder taskletStepBuilder = new TaskletStepBuilder(new StepBuilder(STEP_NAME, jobRepository));
        return taskletStepBuilder
                .tasklet((contribution, chunkContext) -> {
                    JPAQueryFactory queryFactory = new JPAQueryFactory(emf.createEntityManager());
                    brawlStarsClient.getBrawlers().items().forEach(brawlerResponse -> {
                        log.debug("[Brawler: {}] 동기화 여부 확인", brawlerResponse.name());
                        if (checkBrawler(brawlerResponse, queryFactory)) {
                            return;
                        }
                        checkStarPower(brawlerResponse, queryFactory);
                        checkGadget(brawlerResponse, queryFactory);
                    });
                    return RepeatStatus.FINISHED;
                }, txManager).build();
    }

    private boolean checkBrawler(BrawlerResponse brawlerResponse, JPAQueryFactory queryFactory) {
        BrawlerCollectionEntity brawlerEntity = queryFactory.selectFrom(brawlerCollectionEntity)
                .where(brawlerCollectionEntity.brawlStarsId.eq(brawlerResponse.id()))
                .fetchOne();
        if (brawlerEntity == null) {
            log.info("Brawler[{}({})] 업데이트가 필요합니다. 브롤러 정보: {}",
                    brawlerResponse.name(), brawlerResponse.id(), brawlerResponse);
            return true;
        }
        return false;
    }

    private void checkStarPower(BrawlerResponse brawlerResponse, JPAQueryFactory queryFactory) {
        List<StarPowerCollectionEntity> starPowerEntities = queryFactory
                .selectFrom(starPowerCollectionEntity)
                .join(brawlerCollectionEntity).on(
                        brawlerCollectionEntity.id.eq(starPowerCollectionEntity.brawlerId))
                .fetch();
        brawlerResponse.starPowers().forEach(starPowerResponse -> {
            if (starPowerEntities.stream().noneMatch(
                    entity -> entity.getBrawlStarsId() == starPowerResponse.id())) {
                log.info("Brawler[{}({})]의 스타파워 업데이트가 필요합니다. 스타파워 정보: {}",
                        brawlerResponse.name(), brawlerResponse.id(), starPowerResponse);
            }
        });
    }

    private void checkGadget(BrawlerResponse brawlerResponse, JPAQueryFactory queryFactory) {
        List<GadgetCollectionEntity> gadgetEntities = queryFactory
                .selectFrom(gadgetCollectionEntity)
                .join(brawlerCollectionEntity).on(
                        brawlerCollectionEntity.id.eq(gadgetCollectionEntity.brawlerId))
                .fetch();
        brawlerResponse.gadgets().forEach(gadgetResponse -> {
            if (gadgetEntities.stream().noneMatch(
                    entity -> entity.getBrawlStarsId() == gadgetResponse.id())) {
                log.info("Brawler[{}({})]의 가젯 업데이트가 필요합니다. 가젯 정보: {}",
                        brawlerResponse.name(), brawlerResponse.id(), gadgetResponse);
            }
        });
    }

}
