package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.statistics.BattleEventResultCounts;
import com.imstargg.core.domain.statistics.BattleEventResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerEnemyResultCounts;
import com.imstargg.core.domain.statistics.BrawlerEnemyResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerResultCounts;
import com.imstargg.core.domain.statistics.BrawlerResultStatistics;
import com.imstargg.core.domain.statistics.BrawlersResultCounts;
import com.imstargg.core.domain.statistics.BrawlersResultStatistics;
import com.imstargg.core.domain.statistics.StatisticsCache;
import com.imstargg.core.support.FutureUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrawlerStatisticsReaderWithCache {

    private final BrawlerStatisticsCountReaderWithAsync reader;
    private final StatisticsCache cache;

    public BrawlerStatisticsReaderWithCache(
            BrawlerStatisticsCountReaderWithAsync reader,
            StatisticsCache cache
    ) {
        this.reader = reader;
        this.cache = cache;
    }

    public List<BrawlerResultStatistics> getBrawlerResultStatistics(
            BrawlerResultStatisticsParam param
    ) {
        return cache.find(param)
                .orElseGet(() -> {
                    List<BrawlerResultCounts> countsList = FutureUtils.get(param.toCountParams().stream()
                            .map(reader::getBrawlerResultCounts)
                            .toList());

                    BrawlerResultCounts mergedCounts = countsList.stream()
                            .reduce(BrawlerResultCounts::merge)
                            .orElseGet(BrawlerResultCounts::empty);

                    List<BrawlerResultStatistics> statistics = mergedCounts.toStatistics();
                    cache.set(param, statistics);
                    return statistics;
                });
    }

    public List<BattleEventResultStatistics> getBrawlerBattleEventResultStatistics(
            BrawlerBattleEventResultStatisticsParam param
    ) {
        return cache.find(param)
                .orElseGet(() -> {
                    List<BattleEventResultCounts> countsList = FutureUtils.get(param.toCountParams().stream()
                            .map(reader::getBrawlerBattleEventResultCounts)
                            .toList());

                    BattleEventResultCounts mergedCounts = countsList.stream()
                            .reduce(BattleEventResultCounts::merge)
                            .orElseGet(BattleEventResultCounts::empty);

                    List<BattleEventResultStatistics> statistics = mergedCounts.toStatistics();
                    cache.set(param, statistics);
                    return statistics;
                });
    }

    public List<BrawlersResultStatistics> getBrawlerBrawlersResultStatistics(
            BrawlerBrawlersResultStatisticsParam param
    ) {
        return cache.find(param)
                .orElseGet(() -> {
                    List<BrawlersResultCounts> countsList = FutureUtils.get(param.toCountParams().stream()
                            .map(reader::getBrawlerBrawlersResultCounts)
                            .toList());

                    BrawlersResultCounts mergedCounts = countsList.stream()
                            .reduce(BrawlersResultCounts::merge)
                            .orElseGet(BrawlersResultCounts::empty);

                    List<BrawlersResultStatistics> statistics = mergedCounts.toStatistics();
                    cache.set(param, statistics);
                    return statistics;
                });
    }

    public List<BrawlerEnemyResultStatistics> getBrawlerEnemyResultStatistics(
            BrawlerEnemyResultStatisticsParam param
    ) {
        return cache.find(param)
                .orElseGet(() -> {
                    List<BrawlerEnemyResultCounts> countsList = FutureUtils.get(param.toCountParams().stream()
                            .map(reader::getBrawlerEnemyResultCounts)
                            .toList());

                    BrawlerEnemyResultCounts mergedCounts = countsList.stream()
                            .reduce(BrawlerEnemyResultCounts::merge)
                            .orElseGet(BrawlerEnemyResultCounts::empty);

                    List<BrawlerEnemyResultStatistics> statistics = mergedCounts.toStatistics();
                    cache.set(param, statistics);
                    return statistics;
                });
    }
}
