package com.imstargg.batch.job;

import com.imstargg.batch.domain.BrawlerBattleResultStatisticsProcessorWithCache;
import com.imstargg.batch.domain.BrawlersBattleResultStatisticsProcessorWithCache;
import com.imstargg.storage.db.core.BattleCollectionEntity;
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

import java.util.List;

public class BrawlerResultStatisticsJobItemWriter implements ItemWriter<BattleCollectionEntity>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(BrawlerResultStatisticsJobItemWriter.class);

    private final JpaItemWriter<BrawlerBattleResultStatisticsCollectionEntity> brawlerBattleResultItemWriter;
    private final JpaItemWriter<BrawlersBattleResultStatisticsCollectionEntity> brawlersBattleResultItemWriter;
    private final BrawlerBattleResultStatisticsProcessorWithCache brawlerBattleResultStatisticsProcessor;
    private final BrawlersBattleResultStatisticsProcessorWithCache brawlersBattleResultStatisticsProcessor;

    public BrawlerResultStatisticsJobItemWriter(
            JpaItemWriter<BrawlerBattleResultStatisticsCollectionEntity> brawlerBattleResultItemWriter,
            JpaItemWriter<BrawlersBattleResultStatisticsCollectionEntity> brawlersBattleResultItemWriter,
            BrawlerBattleResultStatisticsProcessorWithCache brawlerBattleResultStatisticsProcessor,
            BrawlersBattleResultStatisticsProcessorWithCache brawlersBattleResultStatisticsProcessor
    ) {
        this.brawlerBattleResultItemWriter = brawlerBattleResultItemWriter;
        this.brawlersBattleResultItemWriter = brawlersBattleResultItemWriter;
        this.brawlerBattleResultStatisticsProcessor = brawlerBattleResultStatisticsProcessor;
        this.brawlersBattleResultStatisticsProcessor = brawlersBattleResultStatisticsProcessor;
    }

    public BrawlerResultStatisticsJobItemWriter(
            EntityManagerFactory emf,
            BrawlerBattleResultStatisticsProcessorWithCache brawlerBattleResultStatisticsProcessor,
            BrawlersBattleResultStatisticsProcessorWithCache brawlersBattleResultStatisticsProcessor
    ) {
        this(
                new JpaItemWriterBuilder<BrawlerBattleResultStatisticsCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build(),
                new JpaItemWriterBuilder<BrawlersBattleResultStatisticsCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(false)
                        .build(),
                brawlerBattleResultStatisticsProcessor,
                brawlersBattleResultStatisticsProcessor
        );
    }

    @Override
    public void write(Chunk<? extends BattleCollectionEntity> chunk) throws Exception {
        List<? extends BattleCollectionEntity> battles = chunk.getItems();

        var brawlerBattleResultList = brawlerBattleResultStatisticsProcessor.process(battles);
        var brawlersBattleResultList = brawlersBattleResultStatisticsProcessor.process(battles);

        brawlerBattleResultItemWriter.write(new Chunk<>(brawlerBattleResultList));
        brawlersBattleResultItemWriter.write(new Chunk<>(brawlersBattleResultList));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        brawlerBattleResultItemWriter.afterPropertiesSet();
        brawlersBattleResultItemWriter.afterPropertiesSet();
    }


}
