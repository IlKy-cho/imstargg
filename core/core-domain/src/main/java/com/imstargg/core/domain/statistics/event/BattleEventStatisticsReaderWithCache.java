package com.imstargg.core.domain.statistics.event;

import com.imstargg.core.domain.statistics.BrawlerRankCounts;
import com.imstargg.core.domain.statistics.BrawlerRankStatistics;
import com.imstargg.core.domain.statistics.BrawlerResultCounts;
import com.imstargg.core.domain.statistics.BrawlerResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerPairRankCounts;
import com.imstargg.core.domain.statistics.BrawlerPairRankStatistics;
import com.imstargg.core.domain.statistics.BrawlerPairResultCounts;
import com.imstargg.core.domain.statistics.BrawlerPairResultStatistics;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BattleEventStatisticsReaderWithCache {

    private final BattleEventResultStatisticsRepository battleEventResultStatisticsRepository;
    private final BattleEventRankStatisticsRepository battleEventRankStatisticsRepository;
    private final BattleEventStatisticsCache cache;

    public BattleEventStatisticsReaderWithCache(
            BattleEventResultStatisticsRepository battleEventResultStatisticsRepository,
            BattleEventRankStatisticsRepository battleEventRankStatisticsRepository,
            BattleEventStatisticsCache cache
    ) {
        this.battleEventResultStatisticsRepository = battleEventResultStatisticsRepository;
        this.battleEventRankStatisticsRepository = battleEventRankStatisticsRepository;
        this.cache = cache;
    }

    public List<BrawlerResultStatistics> getBattleEventBrawlerResultStatistics(
            BattleEventBrawlerResultStatisticsParam param
    ) {
        return cache.get(param, key -> {
            var counts = new BrawlerResultCounts(battleEventResultStatisticsRepository.findBrawlerResultCounts(
                    key.eventId(), key.tierRange(), key.startDate(), key.endDate()
            ));
            return counts.toStatistics();
        });
    }

    public List<BrawlerPairResultStatistics> getBattleEventBrawlerPairResultStatistics(
            BattleEventBrawlerPairResultStatisticsParam param
    ) {
        return cache.get(param, key -> {
            var counts = new BrawlerPairResultCounts(battleEventResultStatisticsRepository.findBrawlerPairResultCounts(
                    key.eventId(), key.brawlerId(), key.tierRange(), key.startDate(), key.endDate()
            ));
            return counts.toStatistics();
        });
    }

    public List<BrawlerPairResultStatistics> getBattleEventBrawlerEnemyResultStatistics(
            BattleEventBrawlerEnemyResultStatisticsParam param
    ) {
        return cache.get(param, key -> {
            var counts = new BrawlerPairResultCounts(battleEventResultStatisticsRepository.findBrawlerEnemyResultCounts(
                    key.eventId(), key.brawlerId(), key.tierRange(), key.startDate(), key.endDate()
            ));
            return counts.toStatistics();
        });
    }

    public List<BrawlerRankStatistics> getBattleEventBrawlerRankStatistics(
            BattleEventBrawlerRankStatisticsParam param
    ) {
        return cache.get(param, key -> {
            var counts = new BrawlerRankCounts(battleEventRankStatisticsRepository.findBrawlerRankCounts(
                    key.eventId(), key.trophyRange(), key.startDate(), key.endDate()
            ));
            return counts.toStatistics();
        });
    }

    public List<BrawlerPairRankStatistics> getBattleEventBrawlersRankStatistics(
            BattleEventBrawlerPairRankStatisticsParam param
    ) {
        return cache.get(param, key -> {
            var counts = new BrawlerPairRankCounts(battleEventRankStatisticsRepository.findBrawlerPairRankCounts(
                    key.eventId(), key.brawlerId(), key.trophyRange(), key.startDate(), key.endDate()
            ));
            return counts.toStatistics();
        });
    }
}
