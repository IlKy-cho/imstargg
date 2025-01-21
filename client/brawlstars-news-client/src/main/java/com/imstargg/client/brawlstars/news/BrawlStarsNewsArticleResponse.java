package com.imstargg.client.brawlstars.news;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;

public record BrawlStarsNewsArticleResponse(
        @Nullable String descriptionForNewsArchive,
        String category,
        String showCategoryInTitle,
        BrawlStarsNewArticleThumbnailResponse thumbnail,
        String title,
        String linkUrl,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        OffsetDateTime publishDate,
        boolean isSmallNews,
        String locale
) {

    public BrawlStarsNewsArticleResponse toFullLinkUrl(URI baseUrl) {
        return new BrawlStarsNewsArticleResponse(
                this.descriptionForNewsArchive,
                this.category,
                this.showCategoryInTitle,
                this.thumbnail,
                this.title,
                UriComponentsBuilder.fromUri(baseUrl)
                        .path(this.linkUrl)
                        .build()
                        .toString(),
                this.publishDate,
                this.isSmallNews,
                this.locale
        );
    }
}
