package com.imstargg.batch.job;

import com.imstargg.batch.domain.BrawlerBattleResultStatisticsProcessor;
import com.imstargg.batch.domain.BrawlersBattleResultStatisticsProcessor;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.InitializingBean;

/**
 * 업데이트 로직이 필요할 경우, job 의 date 를 통해 해당 날짜의 데이터 삭제하고 다시 insert 하는 방식으로 처리 해야함
 * 인덱스도 필요함
 */
public class BrawlerBattleResultStatisticsJobItemWriter implements ItemWriter<BattleCollectionEntity>, InitializingBean {

    private final JpaItemWriter<BrawlerBattleResultStatisticsCollectionEntity> brawlerBattleResultItemWriter;
    private final JpaItemWriter<BrawlersBattleResultStatisticsCollectionEntity> brawlersBattleResultItemWriter;
    private final BrawlerBattleResultStatisticsProcessor brawlerBattleResultStatisticsProcessor;
    private final BrawlersBattleResultStatisticsProcessor brawlersBattleResultStatisticsProcessor;

    public BrawlerBattleResultStatisticsJobItemWriter(
            JpaItemWriter<BrawlerBattleResultStatisticsCollectionEntity> brawlerBattleResultItemWriter,
            JpaItemWriter<BrawlersBattleResultStatisticsCollectionEntity> brawlersBattleResultItemWriter,
            BrawlerBattleResultStatisticsProcessor brawlerBattleResultStatisticsProcessor,
            BrawlersBattleResultStatisticsProcessor brawlersBattleResultStatisticsProcessor
    ) {
        this.brawlerBattleResultItemWriter = brawlerBattleResultItemWriter;
        this.brawlersBattleResultItemWriter = brawlersBattleResultItemWriter;
        this.brawlerBattleResultStatisticsProcessor = brawlerBattleResultStatisticsProcessor;
        this.brawlersBattleResultStatisticsProcessor = brawlersBattleResultStatisticsProcessor;
    }

    public BrawlerBattleResultStatisticsJobItemWriter(
            EntityManagerFactory emf,
            BrawlerBattleResultStatisticsProcessor brawlerBattleResultStatisticsProcessor,
            BrawlersBattleResultStatisticsProcessor brawlersBattleResultStatisticsProcessor
    ) {
        this(
                new JpaItemWriterBuilder<BrawlerBattleResultStatisticsCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build(),
                new JpaItemWriterBuilder<BrawlersBattleResultStatisticsCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build(),
                brawlerBattleResultStatisticsProcessor,
                brawlersBattleResultStatisticsProcessor
        );
    }

    @Override
    public void write(Chunk<? extends BattleCollectionEntity> chunk) throws Exception {
        chunk.getItems().forEach(battle -> {
            brawlerBattleResultStatisticsProcessor.process(battle);
            brawlersBattleResultStatisticsProcessor.process(battle);
        });
    }

    @PreDestroy
    void writeStats() {
        brawlerBattleResultItemWriter.write(
                new Chunk<>(brawlerBattleResultStatisticsProcessor.result())
        );
        brawlersBattleResultItemWriter.write(
                new Chunk<>(brawlersBattleResultStatisticsProcessor.result())
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        brawlerBattleResultItemWriter.afterPropertiesSet();
        brawlersBattleResultItemWriter.afterPropertiesSet();
    }


}
