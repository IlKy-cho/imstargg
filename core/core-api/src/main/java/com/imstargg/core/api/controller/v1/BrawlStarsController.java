package com.imstargg.core.api.controller.v1;

import com.imstargg.core.api.controller.response.SliceResponse;
import com.imstargg.core.api.controller.v1.request.PageRequest;
import com.imstargg.core.api.controller.v1.response.BrawlStarsNewsResponse;
import com.imstargg.core.domain.brawlstars.BrawlStarsNewsPageParam;
import com.imstargg.core.domain.brawlstars.BrawlStarsNewsService;
import com.imstargg.core.enums.Language;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrawlStarsController {

    private final BrawlStarsNewsService brawlStarsNewsService;

    public BrawlStarsController(BrawlStarsNewsService brawlStarsNewsService) {
        this.brawlStarsNewsService = brawlStarsNewsService;
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
}
