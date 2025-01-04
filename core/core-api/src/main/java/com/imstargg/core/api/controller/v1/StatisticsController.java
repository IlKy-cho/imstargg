package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.response.ListResponse;
import com.imstargg.core.api.controller.v1.request.BattleEventResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.response.BattleEventBrawlerResultStatisticsResponse;
import com.imstargg.core.domain.statistics.BattleEventStatisticsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private final BattleEventStatisticsService battleEventStatisticsService;

    public StatisticsController(BattleEventStatisticsService battleEventStatisticsService) {
        this.battleEventStatisticsService = battleEventStatisticsService;
    }

    @GetMapping("/api/v1/statistics/battle-event")
    public ListResponse<BattleEventBrawlerResultStatisticsResponse> getBattleEventResultStatistics(
            @ModelAttribute @Validated BattleEventResultStatisticsRequest request
    ) {
        return new ListResponse<>(
                battleEventStatisticsService.getBattleEventResultStatistics(request.toParam())
                        .stream()
                        .map(BattleEventBrawlerResultStatisticsResponse::of)
                        .toList()
        );
    }

}
