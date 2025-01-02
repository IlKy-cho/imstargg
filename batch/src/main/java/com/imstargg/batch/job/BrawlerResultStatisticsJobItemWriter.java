package com.imstargg.batch.job;

import com.imstargg.batch.domain.BattleResultStatisticsCollectedFilter;
import com.imstargg.batch.domain.BrawlerBattleResultStatisticsProcessorWithCache;
import com.imstargg.batch.domain.BrawlersBattleResultStatisticsProcessorWithCache;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.StatisticsCollectedBattleResultCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.InitializingBean;

public class BrawlerResultStatisticsJobItemWriter implements ItemWriter<BattleCollectionEntity>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(BrawlerResultStatisticsJobItemWriter.class);

    private final JpaItemWriter<StatisticsCollectedBattleResultCollectionEntity> battleStatisticsCollectedItemWriter;
    private final JpaItemWriter<BrawlerBattleResultStatisticsCollectionEntity> brawlerBattleResultItemWriter;
    private final JpaItemWriter<BrawlersBattleResultStatisticsCollectionEntity> brawlersBattleResultItemWriter;
    private final BattleResultStatisticsCollectedFilter battleResultStatisticsCollectedFilter;
    private final BrawlerBattleResultStatisticsProcessorWithCache brawlerBattleResultStatisticsProcessor;
    private final BrawlersBattleResultStatisticsProcessorWithCache brawlersBattleResultStatisticsProcessor;

    public BrawlerResultStatisticsJobItemWriter(
            JpaItemWriter<StatisticsCollectedBattleResultCollectionEntity> battleStatisticsCollectedItemWriter,
            JpaItemWriter<BrawlerBattleResultStatisticsCollectionEntity> brawlerBattleResultItemWriter,
            JpaItemWriter<BrawlersBattleResultStatisticsCollectionEntity> brawlersBattleResultItemWriter,
            BattleResultStatisticsCollectedFilter battleResultStatisticsCollectedFilter,
            BrawlerBattleResultStatisticsProcessorWithCache brawlerBattleResultStatisticsProcessor,
            BrawlersBattleResultStatisticsProcessorWithCache brawlersBattleResultStatisticsProcessor
    ) {
        this.battleStatisticsCollectedItemWriter = battleStatisticsCollectedItemWriter;
        this.brawlerBattleResultItemWriter = brawlerBattleResultItemWriter;
        this.brawlersBattleResultItemWriter = brawlersBattleResultItemWriter;
        this.battleResultStatisticsCollectedFilter = battleResultStatisticsCollectedFilter;
        this.brawlerBattleResultStatisticsProcessor = brawlerBattleResultStatisticsProcessor;
        this.brawlersBattleResultStatisticsProcessor = brawlersBattleResultStatisticsProcessor;
    }

    public BrawlerResultStatisticsJobItemWriter(
            EntityManagerFactory emf,
            BattleResultStatisticsCollectedFilter battleResultStatisticsCollectedFilter,
            BrawlerBattleResultStatisticsProcessorWithCache brawlerBattleResultStatisticsProcessor,
            BrawlersBattleResultStatisticsProcessorWithCache brawlersBattleResultStatisticsProcessor
    ) {
        this(
                new JpaItemWriterBuilder<StatisticsCollectedBattleResultCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build(),
                new JpaItemWriterBuilder<BrawlerBattleResultStatisticsCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build(),
                new JpaItemWriterBuilder<BrawlersBattleResultStatisticsCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build(),
                battleResultStatisticsCollectedFilter,
                brawlerBattleResultStatisticsProcessor,
                brawlersBattleResultStatisticsProcessor
        );
    }

    @Override
    public void write(Chunk<? extends BattleCollectionEntity> chunk) throws Exception {
        var battles = battleResultStatisticsCollectedFilter.filter(chunk.getItems());

        var brawlerBattleResultList = brawlerBattleResultStatisticsProcessor.process(battles);
        var brawlersBattleResultList = brawlersBattleResultStatisticsProcessor.process(battles);
        var collectedBattleList = battles.stream()
                .map(battle -> new StatisticsCollectedBattleResultCollectionEntity(battle.getBattleKey()))
                .toList();

        battleStatisticsCollectedItemWriter.write(new Chunk<>(collectedBattleList));
        brawlerBattleResultItemWriter.write(new Chunk<>(brawlerBattleResultList));
        brawlersBattleResultItemWriter.write(new Chunk<>(brawlersBattleResultList));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        battleStatisticsCollectedItemWriter.afterPropertiesSet();
        brawlerBattleResultItemWriter.afterPropertiesSet();
        brawlersBattleResultItemWriter.afterPropertiesSet();
    }


}
