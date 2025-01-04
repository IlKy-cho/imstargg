package com.imstargg.core.domain.statistics;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BattleEventStatisticsService {

    private final BattleEventStatisticsReader battleEventStatisticsReader;

    public BattleEventStatisticsService(BattleEventStatisticsReader battleEventStatisticsReader) {
        this.battleEventStatisticsReader = battleEventStatisticsReader;
    }

    public List<BattleEventBrawlerResultStatistics> getBattleEventResultStatistics(BattleEventResultStatisticsParam param) {
        return battleEventStatisticsReader.getBattleEventResultStatistics(param);
    }
}
