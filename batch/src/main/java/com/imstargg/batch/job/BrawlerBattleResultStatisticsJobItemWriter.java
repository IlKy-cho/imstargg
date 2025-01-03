package com.imstargg.batch.job;

import com.imstargg.batch.domain.BrawlerBattleResultStatisticsCollector;
import com.imstargg.batch.domain.BrawlersBattleResultStatisticsCollector;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class BrawlerBattleResultStatisticsJobItemWriter implements ItemWriter<BattleCollectionEntity> {

    private final BrawlerBattleResultStatisticsCollector brawlerBattleResultStatisticsCollector;
    private final BrawlersBattleResultStatisticsCollector brawlersBattleResultStatisticsCollector;

    public BrawlerBattleResultStatisticsJobItemWriter(
            BrawlerBattleResultStatisticsCollector brawlerBattleResultStatisticsCollector,
            BrawlersBattleResultStatisticsCollector brawlersBattleResultStatisticsCollector
    ) {
        this.brawlerBattleResultStatisticsCollector = brawlerBattleResultStatisticsCollector;
        this.brawlersBattleResultStatisticsCollector = brawlersBattleResultStatisticsCollector;
    }
    @Override
    public void write(Chunk<? extends BattleCollectionEntity> chunk) throws Exception {
        chunk.getItems().forEach(battle -> {
            brawlerBattleResultStatisticsCollector.collect(battle);
            brawlersBattleResultStatisticsCollector.collect(battle);
        });
    }

}
