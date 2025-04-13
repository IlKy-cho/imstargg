package com.imstargg.batch.job.statistics;

import com.imstargg.batch.domain.statistics.BattleItemReader;
import com.imstargg.batch.domain.statistics.BattleItemReaderFactory;
import com.imstargg.batch.domain.statistics.StatisticsCheckPointer;
import com.imstargg.batch.domain.statistics.StatisticsCollector;
import com.imstargg.batch.domain.statistics.StatisticsCollectorFactory;
import com.imstargg.storage.db.core.player.BattleCollectionEntity;
import com.imstargg.storage.db.core.statistics.StatisticsCheckPointCollectionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDate;
import java.util.List;

public class StatisticsJobItemProcessor<T> implements ItemProcessor<LocalDate, List<T>> {

    private static final Logger log = LoggerFactory.getLogger(StatisticsJobItemProcessor.class);
    private static final int PAGE_SIZE = 10000;

    private final String jobName;
    private final StatisticsCheckPointer statisticsCheckPointer;
    private final BattleItemReaderFactory battleItemReaderFactory;
    private final StatisticsCollectorFactory<T> collectorFactory;

    public StatisticsJobItemProcessor(
            String jobName,
            StatisticsCheckPointer statisticsCheckPointer,
            BattleItemReaderFactory battleItemReaderFactory,
            StatisticsCollectorFactory<T> collectorFactory
    ) {
        this.jobName = jobName;
        this.statisticsCheckPointer = statisticsCheckPointer;
        this.battleItemReaderFactory = battleItemReaderFactory;
        this.collectorFactory = collectorFactory;
    }

    @Override
    public List<T> process(LocalDate item) throws Exception {
        StatisticsCollector<T> collector = collectorFactory.create(item);
        StatisticsCheckPointCollectionEntity checkPoint = statisticsCheckPointer.get(jobName, item);
        BattleItemReader battleItemReader = battleItemReaderFactory.create(checkPoint);
        int page = 0;
        boolean hasNext = true;
        int collectedCount = 0;
        int totalCount = 0;
        while (hasNext) {
            log.debug("Statistics[{}] processing date[{}] page[{}]", jobName, item, page);
            List<BattleCollectionEntity> battles = battleItemReader.read(page, PAGE_SIZE);
            if (battles.size() < PAGE_SIZE) {
                hasNext = false;
            }
            page++;

            for (BattleCollectionEntity battle : battles) {
                checkPoint.updateBattleId(battle.getId());
                totalCount++;
                if (collector.collect(battle)) {
                    collectedCount++;
                }
            }
        }

        List<T> statistics = collector.getStatistics();
        log.info("Statistics[{}] collect {}/{} items in date[{}] >> Total {} case statistics",
                jobName, collectedCount, totalCount, item, statistics.stream());

        statisticsCheckPointer.save(checkPoint);
        return statistics;
    }
}
