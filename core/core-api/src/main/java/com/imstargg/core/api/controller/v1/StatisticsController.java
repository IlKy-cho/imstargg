package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.response.ListResponse;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlerRankStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlerResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlersRankStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlersResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.response.BattleEventBrawlerRankStatisticsResponse;
import com.imstargg.core.api.controller.v1.response.BattleEventBrawlerResultStatisticsResponse;
import com.imstargg.core.api.controller.v1.response.BattleEventBrawlersRankStatisticsResponse;
import com.imstargg.core.api.controller.v1.response.BattleEventBrawlersResultStatisticsResponse;
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

    @GetMapping("/api/v1/statistics/battle-event/result/brawler")
    public ListResponse<BattleEventBrawlerResultStatisticsResponse> getBattleEventResultBrawlerStatistics(
            @ModelAttribute @Validated BattleEventBrawlerResultStatisticsRequest request
    ) {
        return new ListResponse<>(
                battleEventStatisticsService.getBattleEventBrawlerResultStatistics(request.toParams())
                        .stream()
                        .map(BattleEventBrawlerResultStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/battle-event/result/brawlers")
    public ListResponse<BattleEventBrawlersResultStatisticsResponse> getBattleEventResultBrawlersStatistics(
            @ModelAttribute @Validated BattleEventBrawlersResultStatisticsRequest request
    ) {
        return new ListResponse<>(
                battleEventStatisticsService.getBattleEventBrawlersResultStatistics(request.toParams())
                        .stream()
                        .map(BattleEventBrawlersResultStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/battle-event/rank/brawler")
    public ListResponse<BattleEventBrawlerRankStatisticsResponse> getBattleEventRankBrawlerStatistics(
            @ModelAttribute @Validated BattleEventBrawlerRankStatisticsRequest request
    ) {
        return new ListResponse<>(
                battleEventStatisticsService.getBattleEventBrawlerRankStatistics(request.toParams())
                        .stream()
                        .map(BattleEventBrawlerRankStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/battle-event/rank/brawlers")
    public ListResponse<BattleEventBrawlersRankStatisticsResponse> getBattleEventRankBrawlersStatistics(
            @ModelAttribute @Validated BattleEventBrawlersRankStatisticsRequest request
    ) {
        return new ListResponse<>(
                battleEventStatisticsService.getBattleEventBrawlersRankStatistics(request.toParams())
                        .stream()
                        .map(BattleEventBrawlersRankStatisticsResponse::of)
                        .toList()
        );
    }
}
