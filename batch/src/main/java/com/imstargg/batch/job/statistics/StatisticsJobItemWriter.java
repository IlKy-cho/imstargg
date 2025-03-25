package com.imstargg.batch.job.statistics;

import com.imstargg.batch.domain.statistics.StatisticsCollector;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class StatisticsJobItemWriter<T> implements ItemWriter<BattleCollectionEntity> {

    private static final Logger log = LoggerFactory.getLogger(StatisticsJobItemWriter.class);

    private final StatisticsCollector<T> collector;

    public StatisticsJobItemWriter(StatisticsCollector<T> collector) {
        this.collector = collector;
    }

    @Override
    public void write(Chunk<? extends BattleCollectionEntity> chunk) throws Exception {
        int collectedCount = 0;
        for (BattleCollectionEntity item : chunk) {
            if (collector.collect(item)) {
                collectedCount++;
            }
        }
        log.debug("Statistics collect {}/{} items in chunk", collectedCount, chunk.size());
    }
}
