package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.response.ListResponse;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlerRankStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlerResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlersRankStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlersResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BrawlerResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.response.BrawlerRankStatisticsResponse;
import com.imstargg.core.api.controller.v1.response.BrawlerResultStatisticsResponse;
import com.imstargg.core.api.controller.v1.response.BrawlersRankStatisticsResponse;
import com.imstargg.core.api.controller.v1.response.BrawlersResultStatisticsResponse;
import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.BattleEventStatisticsService;
import com.imstargg.core.domain.statistics.BrawlerStatisticsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private final BattleEventStatisticsService battleEventStatisticsService;
    private final BrawlerStatisticsService brawlerStatisticsService;

    public StatisticsController(
            BattleEventStatisticsService battleEventStatisticsService,
            BrawlerStatisticsService brawlerStatisticsService
    ) {
        this.battleEventStatisticsService = battleEventStatisticsService;
        this.brawlerStatisticsService = brawlerStatisticsService;
    }

    @GetMapping("/api/v1/statistics/events/{eventId}/result/brawler")
    public ListResponse<BrawlerResultStatisticsResponse> getBattleEventResultBrawlerStatistics(
            @PathVariable long eventId,
            @ModelAttribute @Validated BattleEventBrawlerResultStatisticsRequest request
    ) {
        return new ListResponse<>(
                battleEventStatisticsService.getBattleEventBrawlerResultStatistics(
                                request.toParams(new BrawlStarsId(eventId)))
                        .stream()
                        .map(BrawlerResultStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/events/{eventId}/result/brawlers")
    public ListResponse<BrawlersResultStatisticsResponse> getBattleEventResultBrawlersStatistics(
            @PathVariable long eventId,
            @ModelAttribute @Validated BattleEventBrawlersResultStatisticsRequest request
    ) {
        return new ListResponse<>(
                battleEventStatisticsService.getBattleEventBrawlersResultStatistics(
                                request.toParams(new BrawlStarsId(eventId)))
                        .stream()
                        .map(BrawlersResultStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/events/{eventId}/rank/brawler")
    public ListResponse<BrawlerRankStatisticsResponse> getBattleEventRankBrawlerStatistics(
            @PathVariable long eventId,
            @ModelAttribute @Validated BattleEventBrawlerRankStatisticsRequest request
    ) {
        return new ListResponse<>(
                battleEventStatisticsService.getBattleEventBrawlerRankStatistics(
                                request.toParams(new BrawlStarsId(eventId)))
                        .stream()
                        .map(BrawlerRankStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/events/{eventId}/rank/brawlers")
    public ListResponse<BrawlersRankStatisticsResponse> getBattleEventRankBrawlersStatistics(
            @PathVariable long eventId,
            @ModelAttribute @Validated BattleEventBrawlersRankStatisticsRequest request
    ) {
        return new ListResponse<>(
                battleEventStatisticsService.getBattleEventBrawlersRankStatistics(
                                request.toParams(new BrawlStarsId(eventId)))
                        .stream()
                        .map(BrawlersRankStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/brawler-result")
    public ListResponse<BrawlerResultStatisticsResponse> getBrawlerResultStatistics(
            @ModelAttribute @Validated BrawlerResultStatisticsRequest request
    ) {
        return new ListResponse<>(
                brawlerStatisticsService.getBrawlerResultStatistics(request.toParams())
                        .stream()
                        .map(BrawlerResultStatisticsResponse::of)
                        .toList()
        );
    }
}
