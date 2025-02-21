package com.imstargg.batch.job;

import com.imstargg.batch.job.support.JsonlFileItemWriter;
import com.imstargg.batch.job.support.querydsl.QuerydslEntityCursorItemReader;
import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionEntityTeamPlayer;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.imstargg.storage.db.core.QBattleCollectionEntity.battleCollectionEntity;
import static com.imstargg.storage.db.core.brawlstars.QSoloRankBattleEventCollectionEntity.soloRankBattleEventCollectionEntity;

@Configuration
public class SoloRankBattleDownloadJobConfig {

    private static final String JOB_NAME = "soloRankBattleDownloadJob";
    private static final String STEP_NAME = "soloRankBattleDownloadStep";
    private static final int CHUNK_SIZE = 10000;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    public SoloRankBattleDownloadJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<BattleCollectionEntity, BattleItem>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    QuerydslEntityCursorItemReader<BattleCollectionEntity> reader() {
        var reader = new QuerydslEntityCursorItemReader<BattleCollectionEntity>(emf, CHUNK_SIZE,
                (lastItem, queryFactory) -> queryFactory
                        .selectFrom(battleCollectionEntity)
                        .join(battleCollectionEntity.player.player).fetchJoin()
                        .where(
                                lastItem == null ? null :
                                        (battleCollectionEntity.battleTime.eq(lastItem.getBattleTime()))
                                                .and(battleCollectionEntity.id.gt(lastItem.getId()))
                                                .or(battleCollectionEntity.battleTime.lt(lastItem.getBattleTime()))
                        )
                        .orderBy(
                                battleCollectionEntity.battleTime.desc(),
                                battleCollectionEntity.id.asc()
                        )
        );
        reader.setSaveState(false);
        reader.setTransacted(false);
        return reader;
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    ItemProcessor<BattleCollectionEntity, BattleItem> processor() {
        Set<Long> eventBrawlStarsIds = new HashSet<>(JPAQueryFactoryUtils.getQueryFactory(emf)
                .select(soloRankBattleEventCollectionEntity.event.brawlStarsId)
                .from(soloRankBattleEventCollectionEntity)
                .where(soloRankBattleEventCollectionEntity.deleted.isFalse())
                .fetch());
        return item -> {
            if (!BattleType.SOLO_RANKED.getCode().equals(item.getType())) {
                return null;
            }
            if (!eventBrawlStarsIds.contains(item.getEvent().getBrawlStarsId())) {
                return null;
            }
            BattleResult result = BattleResult.map(item.getResult());
            List<BattleCollectionEntityTeamPlayer> myTeam = item.findMyTeam();
            List<BattleCollectionEntityTeamPlayer> enemyTeam = item.findEnemyTeam();


            return new BattleItem(
                    item.getBattleTime().toEpochSecond(),
                    Objects.requireNonNull(item.getEvent().getBrawlStarsId()),
                    result,
                    myTeam.stream()
                            .map(player -> new BattleItemPlayer(
                                    player.getBrawler().getBrawlStarsId(),
                                    player.getBrawler().getTrophies()
                            )).toList(),
                    enemyTeam.stream()
                            .map(player -> new BattleItemPlayer(
                                    player.getBrawler().getBrawlStarsId(),
                                    player.getBrawler().getTrophies()
                            )).toList()
            );
        };
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    JsonlFileItemWriter<BattleItem> writer() {
        var writer = new JsonlFileItemWriter<BattleItem>();
        writer.setName(STEP_NAME + "ItemWriter");
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        writer.setResource(new FileSystemResource("./data/solo-rank-battle-" + currentTime + ".jsonl"));
        return writer;
    }

    record BattleItem(
            long battleTimestamp,
            long eventId,
            BattleResult result,
            List<BattleItemPlayer> myTeam,
            List<BattleItemPlayer> enemyTeam
    ) {
    }

    record BattleItemPlayer(
            long brawlerId,
            int tier
    ) {
    }
}
