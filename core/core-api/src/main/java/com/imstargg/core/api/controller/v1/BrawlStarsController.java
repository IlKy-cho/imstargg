package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.response.ListResponse;
import com.imstargg.core.api.controller.v1.response.BattleEventResponse;
import com.imstargg.core.api.controller.v1.response.BrawlerResponse;
import com.imstargg.core.api.controller.v1.response.RotationBattleEventResponse;
import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.brawlstars.BrawlStarsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class BrawlStarsController {

    private final BrawlStarsService brawlStarsService;

    public BrawlStarsController(
            BrawlStarsService brawlStarsService
    ) {
        this.brawlStarsService = brawlStarsService;
    }

    @GetMapping("/api/v1/brawlstars/brawlers")
    public ListResponse<BrawlerResponse> getBrawlers() {
        return new ListResponse<>(
                brawlStarsService.getAllBrawlers().stream()
                        .map(BrawlerResponse::from)
                        .toList()
        );
    }

    @GetMapping("/api/v1/brawlstars/brawlers/{brawlerId}")
    public BrawlerResponse getBrawler(@PathVariable long brawlerId) {
        return BrawlerResponse.from(brawlStarsService.getBrawler(new BrawlStarsId(brawlerId)));
    }

    @GetMapping("/api/v1/brawlstars/events")
    public ListResponse<BattleEventResponse> getEvents(@RequestParam LocalDate date) {
        return new ListResponse<>(
                brawlStarsService.getEvents(date).stream()
                        .map(BattleEventResponse::from)
                        .toList()
        );
    }

    @GetMapping("/api/v1/brawlstars/events/{eventId}")
    public BattleEventResponse getEvent(@PathVariable long eventId) {
        return BattleEventResponse.from(brawlStarsService.getEvent(new BrawlStarsId(eventId)));
    }

    @GetMapping("/api/v1/brawlstars/event/rotation-events")
    public ListResponse<RotationBattleEventResponse> getRotationEvents() {
        return new ListResponse<>(
                brawlStarsService.getRotationEvents().stream()
                        .map(RotationBattleEventResponse::of)
                        .toList()
        );
    }

    @GetMapping("/api/v1/brawlstars/event/solo-rank-events")
    public ListResponse<BattleEventResponse> getSoloRankEvents() {
        return new ListResponse<>(
                brawlStarsService.getSoloRankEvents().stream()
                        .map(BattleEventResponse::from)
                        .toList()
        );
    }
}
