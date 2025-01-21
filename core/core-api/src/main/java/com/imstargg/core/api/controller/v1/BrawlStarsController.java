package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.response.ListResponse;
import com.imstargg.core.api.controller.response.SliceResponse;
import com.imstargg.core.api.controller.request.PageRequest;
import com.imstargg.core.api.controller.v1.response.BattleEventResponse;
import com.imstargg.core.api.controller.v1.response.BrawlStarsNewsResponse;
import com.imstargg.core.api.controller.v1.response.BrawlerResponse;
import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.brawlstars.BrawlStarsNewsPageParam;
import com.imstargg.core.domain.brawlstars.BrawlStarsNewsService;
import com.imstargg.core.domain.brawlstars.BrawlStarsService;
import com.imstargg.core.enums.Language;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class BrawlStarsController {

    private final BrawlStarsNewsService brawlStarsNewsService;
    private final BrawlStarsService brawlStarsService;

    public BrawlStarsController(
            BrawlStarsNewsService brawlStarsNewsService,
            BrawlStarsService brawlStarsService
    ) {
        this.brawlStarsNewsService = brawlStarsNewsService;
        this.brawlStarsService = brawlStarsService;
    }

    @GetMapping("/api/v1/brawlstars/news")
    public SliceResponse<BrawlStarsNewsResponse> getNews(
            @ModelAttribute @Validated PageRequest pageRequest, @RequestParam Language language
    ) {
        return SliceResponse.of(
                brawlStarsNewsService.getNews(new BrawlStarsNewsPageParam(language, pageRequest.page()))
                        .map(BrawlStarsNewsResponse::of)
        );
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
}
