package com.imstargg.core.api.controller.v1.response;

import com.imstargg.core.domain.brawlstars.BrawlStarsNews;

import java.time.OffsetDateTime;

public record BrawlStarsNewsResponse(
        String title,
        String linkUrl,
        OffsetDateTime publishDate
) {

    public static BrawlStarsNewsResponse of(BrawlStarsNews brawlStarsNews) {
        return new BrawlStarsNewsResponse(
                brawlStarsNews.title(),
                brawlStarsNews.linkUrl(),
                brawlStarsNews.publishDate()
        );
    }
}
