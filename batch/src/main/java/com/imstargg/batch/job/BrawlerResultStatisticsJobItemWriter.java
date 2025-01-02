package com.imstargg.batch.job;

import com.imstargg.batch.domain.BattleResultStatisticsCollectedFilter;
import com.imstargg.batch.domain.BrawlerBattleResultStatisticsProcessorWithCache;
import com.imstargg.batch.domain.BrawlersBattleResultStatisticsProcessorWithCache;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BattleResultStatisticsCollectedCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultCollectionEntity;
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

    private final JpaItemWriter<BattleResultStatisticsCollectedCollectionEntity> battleStatisticsCollectedItemWriter;
    private final JpaItemWriter<BrawlerBattleResultCollectionEntity> brawlerBattleResultItemWriter;
    private final JpaItemWriter<BrawlersBattleResultCollectionEntity> brawlersBattleResultItemWriter;
    private final BattleResultStatisticsCollectedFilter battleResultStatisticsCollectedFilter;
    private final BrawlerBattleResultStatisticsProcessorWithCache brawlerBattleResultStatisticsProcessor;
    private final BrawlersBattleResultStatisticsProcessorWithCache brawlersBattleResultStatisticsProcessor;

    public BrawlerResultStatisticsJobItemWriter(
            JpaItemWriter<BattleResultStatisticsCollectedCollectionEntity> battleStatisticsCollectedItemWriter,
            JpaItemWriter<BrawlerBattleResultCollectionEntity> brawlerBattleResultItemWriter,
            JpaItemWriter<BrawlersBattleResultCollectionEntity> brawlersBattleResultItemWriter,
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
                new JpaItemWriterBuilder<BattleResultStatisticsCollectedCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build(),
                new JpaItemWriterBuilder<BrawlerBattleResultCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build(),
                new JpaItemWriterBuilder<BrawlersBattleResultCollectionEntity>()
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
        var collectedBattleList = chunk.getItems().stream()
                .map(battle -> new BattleResultStatisticsCollectedCollectionEntity(battle.getBattleKey()))
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
