package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.statistics.BattleEventResultCounts;
import com.imstargg.core.domain.statistics.BattleEventResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerPairResultCounts;
import com.imstargg.core.domain.statistics.BrawlerPairResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerResultCounts;
import com.imstargg.core.domain.statistics.BrawlerResultStatistics;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrawlerStatisticsReaderWithCache {

    private final BrawlerResultStatisticsRepository repository;
    private final BrawlerStatisticsCache cache;

    public BrawlerStatisticsReaderWithCache(
            BrawlerResultStatisticsRepository repository,
            BrawlerStatisticsCache cache
    ) {
        this.repository = repository;
        this.cache = cache;
    }

    public List<BrawlerResultStatistics> getBrawlerResultStatistics(
            BrawlerResultStatisticsParam param
    ) {
        return cache.get(param, key -> {
            var counts = new BrawlerResultCounts(repository.findBrawlerResultCounts(
                    key.tierRange(), key.startDate(), key.endDate()
            ));
            return counts.toStatistics();
        });
    }

    public List<BattleEventResultStatistics> getBrawlerBattleEventResultStatistics(
            BrawlerBattleEventResultStatisticsParam param
    ) {
        return cache.get(param, key -> {
            var counts = new BattleEventResultCounts(repository.findBrawlerBattleEventResultCounts(
                    key.brawlerId(), key.tierRange(), key.startDate(), key.endDate()
            ));
            return counts.toStatistics();
        });
    }

    public List<BrawlerPairResultStatistics> getBrawlerPairResultStatistics(
            BrawlerPairResultStatisticsParam param
    ) {
        return cache.get(param, key -> {
            var counts = new BrawlerPairResultCounts(repository.findBrawlerPairResultCounts(
                    key.brawlerId(), key.tierRange(), key.startDate(), key.endDate()
            ));
            return counts.toStatistics();
        });
    }

    public List<BrawlerPairResultStatistics> getBrawlerEnemyResultStatistics(
            BrawlerEnemyResultStatisticsParam param
    ) {
        return cache.get(param, key -> {
            var counts = new BrawlerPairResultCounts(repository.findBrawlerEnemyResultCounts(
                    key.brawlerId(), key.tierRange(), key.startDate(), key.endDate()
            ));
            return counts.toStatistics();
        });
    }
}
