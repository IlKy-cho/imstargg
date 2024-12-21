package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.BrawlStarsNews;

import java.time.LocalDateTime;

public record BrawlStarsNewsResponse(
        String title,
        String linkUrl,
        LocalDateTime publishDate
) {

    public static BrawlStarsNewsResponse of(BrawlStarsNews brawlStarsNews) {
        return new BrawlStarsNewsResponse(
                brawlStarsNews.title(),
                brawlStarsNews.linkUrl(),
                brawlStarsNews.publishDate()
        );
    }
}
