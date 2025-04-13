package com.imstargg.batch.job.statistics;

import com.imstargg.batch.job.support.ItemListWriter;
import com.imstargg.batch.job.support.JdbcBatchItemInsertUpdateWriter;
import com.imstargg.core.enums.BattleResult;
import com.imstargg.core.enums.TrophyRange;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerPairBattleResultStatisticsCollectionJpaRepository;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Disabled
@Tag("develop")
@ActiveProfiles({"local", "batch"})
@SpringBootTest
class StatisticsItemWriterPerformanceTest {

    private static final Logger log = LoggerFactory.getLogger(StatisticsItemWriterPerformanceTest.class);

    @Autowired
    private BrawlerPairBattleResultStatisticsCollectionJpaRepository repository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private DataSource dataSource;

    @Test
    void test() {
        System.out.println((createEntities().size()));
    }

    @Test
    void JpaItemWriter_insert() throws Exception {
        var writer = new JpaItemWriterBuilder<BrawlerPairBattleResultStatisticsCollectionEntity>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(false)
                .build();
        writer.afterPropertiesSet();
        var entities = createEntities();

        long startTime = System.currentTimeMillis();
        transactionTemplate.execute(status -> {
            writer.write(new Chunk<>(entities));
            return null;
        });
        long endTime = System.currentTimeMillis();
        log.info("{} ms", endTime - startTime);
    }

    @Test
    void JpaItemWriter_update() throws Exception {
        var writer = new JpaItemWriterBuilder<BrawlerPairBattleResultStatisticsCollectionEntity>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(false)
                .build();
        writer.afterPropertiesSet();
        repository.saveAll(createEntities());
        var entities = repository.findAll();
        entities.forEach(entity -> entity.countUp(BattleResult.VICTORY));
        entities.forEach(entity -> entity.countUp(BattleResult.DEFEAT));
        entities.forEach(entity -> entity.countUp(BattleResult.DRAW));

        long startTime = System.currentTimeMillis();
        transactionTemplate.execute(status -> {
            writer.write(new Chunk<>(entities));
            return null;
        });
        long endTime = System.currentTimeMillis();
        log.info("{} ms", endTime - startTime);
    }

    @Test
    void JdbcItemWriter_insert() throws Exception {
        var writer = createJdbcItemWriter();
        writer.afterPropertiesSet();
        var entities = createEntities();

        long startTime = System.currentTimeMillis();
        transactionTemplate.execute(status -> {
            try {
                writer.write(new Chunk<>(List.of(entities)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        });
        long endTime = System.currentTimeMillis();
        log.info("{} ms", endTime - startTime);
    }

    @Test
    void JdbcItemWriter_update() throws Exception {
        var writer = createJdbcItemWriter();
        writer.afterPropertiesSet();
        repository.saveAll(createEntities());
        var entities = repository.findAll();
        entities.forEach(entity -> entity.countUp(BattleResult.VICTORY));
        entities.forEach(entity -> entity.countUp(BattleResult.DEFEAT));
        entities.forEach(entity -> entity.countUp(BattleResult.DRAW));

        long startTime = System.currentTimeMillis();
        transactionTemplate.execute(status -> {
            try {
                writer.write(new Chunk<>(List.of(entities)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return null;
        });
        long endTime = System.currentTimeMillis();
        log.info("{} ms", endTime - startTime);
    }

    private ItemListWriter<BrawlerPairBattleResultStatisticsCollectionEntity> createJdbcItemWriter() {
        return new ItemListWriter<>(
                new JdbcBatchItemInsertUpdateWriter<>(
                        new JdbcBatchItemWriterBuilder<BrawlerPairBattleResultStatisticsCollectionEntity>()
                                .dataSource(dataSource)
                                .beanMapped()
                                .sql("""
                                        INSERT INTO brawler_pair_battle_result_stats_v3
                                        (
                                            event_brawlstars_id,
                                            brawler_brawlstars_id,
                                            tier_range,
                                            battle_date,
                                            pair_brawler_brawlstars_id,
                                            victory_count,
                                            defeat_count,
                                            draw_count,
                                            deleted
                                        )
                                        VALUES
                                        (
                                            :eventBrawlStarsId,
                                            :brawlerBrawlStarsId,
                                            :tierRange,
                                            :battleDate,
                                            :pairBrawlerBrawlStarsId,
                                            :victoryCount,
                                            :defeatCount,
                                            :drawCount,
                                            :deleted
                                        )
                                        """)
                                .build(),
                        new JdbcBatchItemWriterBuilder<BrawlerPairBattleResultStatisticsCollectionEntity>()
                                .dataSource(dataSource)
                                .beanMapped()
                                .sql("""
                                        UPDATE brawler_pair_battle_result_stats_v3
                                        SET
                                            event_brawlstars_id = :eventBrawlStarsId,
                                            brawler_brawlstars_id = :brawlerBrawlStarsId,
                                            tier_range = :tierRange,
                                            battle_date = :battleDate,
                                            pair_brawler_brawlstars_id = :pairBrawlerBrawlStarsId,
                                            victory_count = :victoryCount,
                                            defeat_count = :defeatCount,
                                            draw_count = :drawCount,
                                            deleted = :deleted
                                        WHERE
                                            brawler_pair_battle_result_stats_id = :id
                                        """)
                                .build(),
                        item -> item.getId() == null
                )
        );
    }

    private List<BrawlerPairBattleResultStatisticsCollectionEntity> createEntities() {
        List<BrawlerPairBattleResultStatisticsCollectionEntity> entities = new ArrayList<>();
        LocalDate date = LocalDate.of(2025, 1, 1);
        for (int eventBrawlStarsId = 0; eventBrawlStarsId < 20; eventBrawlStarsId++) {
            for (int brawlerBrawlStarsId = 0; brawlerBrawlStarsId < 15; brawlerBrawlStarsId++) {
                for (int pairBrawlerBrawlStarsId = 0; pairBrawlerBrawlStarsId < 15; pairBrawlerBrawlStarsId++) {
                    for (TrophyRange trophyRange : TrophyRange.values()) {
                        for (LocalDate battleDate : date.datesUntil(date.plusDays(10)).toList()) {
                            entities.add(new BrawlerPairBattleResultStatisticsCollectionEntity(
                                    eventBrawlStarsId,
                                    brawlerBrawlStarsId,
                                    trophyRange,
                                    battleDate,
                                    pairBrawlerBrawlStarsId
                            ));
                        }
                    }
                }
            }
        }
        return entities;
    }
}
