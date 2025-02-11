package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.response.ListResponse;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlerEnemyResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlerRankStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlerResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlersRankStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BattleEventBrawlersResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BrawlerBattleEventResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BrawlerBrawlersResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BrawlerEnemyResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.request.BrawlerResultStatisticsRequest;
import com.imstargg.core.api.controller.v1.response.BattleEventResultStatisticsResponse;
import com.imstargg.core.api.controller.v1.response.BrawlerEnemyResultStatisticsResponse;
import com.imstargg.core.api.controller.v1.response.BrawlerItemOwnershipResponse;
import com.imstargg.core.api.controller.v1.response.BrawlerRankStatisticsResponse;
import com.imstargg.core.api.controller.v1.response.BrawlerResultStatisticsResponse;
import com.imstargg.core.api.controller.v1.response.BrawlersRankStatisticsResponse;
import com.imstargg.core.api.controller.v1.response.BrawlersResultStatisticsResponse;
import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.statistics.brawler.BrawlerStatisticsService;
import com.imstargg.core.domain.statistics.event.BattleEventStatisticsService;
import com.imstargg.core.enums.TrophyRangeRange;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
                                request.toParam(new BrawlStarsId(eventId)))
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
                                request.toParam(new BrawlStarsId(eventId)))
                        .stream()
                        .map(BrawlersResultStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/events/{eventId}/result/brawler-enemy")
    public ListResponse<BrawlerEnemyResultStatisticsResponse> getBattleEventResultBrawlerEnemyStatistics(
            @PathVariable long eventId,
            @ModelAttribute @Validated BattleEventBrawlerEnemyResultStatisticsRequest request
    ) {
        return new ListResponse<>(
                battleEventStatisticsService.getBattleEventBrawlerEnemyResultStatistics(
                                request.toParam(new BrawlStarsId(eventId)))
                        .stream()
                        .map(BrawlerEnemyResultStatisticsResponse::of)
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
                                request.toParam(new BrawlStarsId(eventId)))
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
                                request.toParam(new BrawlStarsId(eventId)))
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
                brawlerStatisticsService.getBrawlerResultStatistics(request.toParam())
                        .stream()
                        .map(BrawlerResultStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/brawlers/{brawlerId}/result")
    public ListResponse<BattleEventResultStatisticsResponse> getBrawlerBattleEventResultStatistics(
            @PathVariable long brawlerId,
            @ModelAttribute @Validated BrawlerBattleEventResultStatisticsRequest request
    ) {
        return new ListResponse<>(
                brawlerStatisticsService.getBrawlerBattleEventResultStatistics(
                                request.toParam(new BrawlStarsId(brawlerId))
                        ).stream()
                        .map(BattleEventResultStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/brawlers/{brawlerId}/brawlers-result")
    public ListResponse<BrawlersResultStatisticsResponse> getBrawlerBrawlersResultStatistics(
            @PathVariable long brawlerId,
            @ModelAttribute @Validated BrawlerBrawlersResultStatisticsRequest request
    ) {
        return new ListResponse<>(
                brawlerStatisticsService.getBrawlerBrawlersResultStatistics(
                                request.toParam(new BrawlStarsId(brawlerId))
                        ).stream()
                        .map(BrawlersResultStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/brawlers/{brawlerId}/enemy-result")
    public ListResponse<BrawlerEnemyResultStatisticsResponse> getBrawlerEnemyResultStatistics(
            @PathVariable long brawlerId,
            @ModelAttribute @Validated BrawlerEnemyResultStatisticsRequest request
    ) {
        return new ListResponse<>(
                brawlerStatisticsService.getBrawlerEnemyResultStatistics(
                                request.toParam(new BrawlStarsId(brawlerId))
                        ).stream()
                        .map(BrawlerEnemyResultStatisticsResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/statistics/brawlers/{brawlerId}/ownership")
    public BrawlerItemOwnershipResponse getBrawlerOwnershipRate(
            @PathVariable long brawlerId, @RequestParam TrophyRangeRange trophyRange
    ) {
        return BrawlerItemOwnershipResponse.of(
                brawlerStatisticsService.getOwnershipRate(new BrawlStarsId(brawlerId), trophyRange)
        );
    }
}
