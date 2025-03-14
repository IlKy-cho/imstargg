package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.statistics.BrawlerEnemyResultCounts;
import com.imstargg.core.domain.statistics.BrawlerEnemyResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerRankCounts;
import com.imstargg.core.domain.statistics.BrawlerRankStatistics;
import com.imstargg.core.domain.statistics.BrawlerResultCounts;
import com.imstargg.core.domain.statistics.BrawlerResultStatistics;
import com.imstargg.core.domain.statistics.BrawlersRankCounts;
import com.imstargg.core.domain.statistics.BrawlersRankStatistics;
import com.imstargg.core.domain.statistics.BrawlersResultCounts;
import com.imstargg.core.domain.statistics.BrawlersResultStatistics;
import com.imstargg.core.domain.statistics.StatisticsCache;
import com.imstargg.core.support.FutureUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleEventStatisticsReaderWithCache {

    private final BattleEventStatisticsCountReaderWithAsync reader;
    private final StatisticsCache cache;

    public BattleEventStatisticsReaderWithCache(
            BattleEventStatisticsCountReaderWithAsync reader,
            StatisticsCache cache
    ) {
        this.reader = reader;
        this.cache = cache;
    }

    public List<BrawlerResultStatistics> getBattleEventBrawlerResultStatistics(
            BattleEventBrawlerResultStatisticsParam params
    ) {
        return cache.find(params)
                .orElseGet(() -> {
                    List<BrawlerResultCounts> countsList = FutureUtils.get(params.toCountParams().stream()
                            .map(reader::getBattleEventBrawlerResultCounts)
                            .toList());

                    BrawlerResultCounts mergedCounts = countsList.stream()
                            .reduce(BrawlerResultCounts::merge)
                            .orElseGet(BrawlerResultCounts::empty);

                    List<BrawlerResultStatistics> statistics = mergedCounts.toStatistics();
                    cache.set(params, statistics);
                    return statistics;
                });
    }

    public List<BrawlersResultStatistics> getBattleEventBrawlersResultStatistics(
            BattleEventBrawlersResultStatisticsParam param) {
        return cache.find(param)
                .orElseGet(() -> {
                    List<BrawlersResultCounts> countsList = FutureUtils.get(param.toCountParams().stream()
                            .map(reader::getBattleEventBrawlersResultCounts)
                            .toList());

                    BrawlersResultCounts mergedCounts = countsList.stream()
                            .reduce(BrawlersResultCounts::merge)
                            .orElseGet(BrawlersResultCounts::empty);

                    List<BrawlersResultStatistics> statistics = mergedCounts.toStatistics();
                    cache.set(param, statistics);
                    return statistics;
                });
    }

    public List<BrawlerRankStatistics> getBattleEventBrawlerRankStatistics(
            BattleEventBrawlerRankStatisticsParam params
    ) {
        return cache.find(params)
                .orElseGet(() -> {
                    List<BrawlerRankCounts> countsList = FutureUtils.get(params.toCountParams().stream()
                            .map(reader::getBattleEventBrawlerRankCounts)
                            .toList());

                    BrawlerRankCounts mergedCounts = countsList.stream()
                            .reduce(BrawlerRankCounts::merge)
                            .orElseGet(BrawlerRankCounts::empty);

                    List<BrawlerRankStatistics> statistics = mergedCounts.toStatistics();
                    cache.set(params, statistics);
                    return statistics;
                });
    }

    public List<BrawlersRankStatistics> getBattleEventBrawlersRankStatistics(
            BattleEventBrawlersRankStatisticsParam params) {
        return cache.find(params)
                .orElseGet(() -> {
                    List<BrawlersRankCounts> countsList = FutureUtils.get(params.toCountParams().stream()
                            .map(reader::getBattleEventBrawlersRankCounts)
                            .toList());

                    BrawlersRankCounts mergedCounts = countsList.stream()
                            .reduce(BrawlersRankCounts::merge)
                            .orElseGet(BrawlersRankCounts::empty);

                    List<BrawlersRankStatistics> statistics = mergedCounts.toStatistics();
                    cache.set(params, statistics);
                    return statistics;
                });
    }

    public List<BrawlerEnemyResultStatistics> getBattleEventBrawlerEnemyResultStatistics(
            BattleEventBrawlerEnemyResultStatisticsParam param) {
        return cache.find(param)
                .orElseGet(() -> {
                    List<BrawlerEnemyResultCounts> countsList = FutureUtils.get(param.toCountParams().stream()
                            .map(reader::getBattleEventBrawlerEnemyResultCounts)
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
