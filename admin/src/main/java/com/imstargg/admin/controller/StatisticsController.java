package com.imstargg.admin.controller;

import com.imstargg.admin.controller.response.ValueResponse;
import com.imstargg.admin.domain.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/admin/api/statistics/battle/latest-id")
    public ValueResponse<Long> getBattleLatestId() {
        return new ValueResponse<>(statisticsService.getBattleLatestId());
    }

    @GetMapping("/admin/api/statistics/player/latest-id")
    public ValueResponse<Long> getPlayerLatestId() {
        return new ValueResponse<>(statisticsService.getPlayerLatestId());
    }
}
