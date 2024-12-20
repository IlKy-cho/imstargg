package com.imstargg.client.brawlstars.news;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;

import java.time.ZonedDateTime;

public record BrawlStarsNewsArticleResponseDto(
        @Nullable String descriptionForNewsArchive,
        String category,
        String showCategoryInTitle,
        BrawlStarsNewArticleThumbnailResponse thumbnail,
        String title,
        String linkUrl,
        @JsonProperty("publishDate")
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        ZonedDateTime publishDateWithZone,
        boolean isSmallNews,
        String locale
) {
    public BrawlStarsNewsArticleResponse toResponse() {
        return new BrawlStarsNewsArticleResponse(
                this.descriptionForNewsArchive,
                this.category,
                this.showCategoryInTitle,
                this.thumbnail,
                this.title,
                this.linkUrl,
                this.publishDateWithZone.toLocalDateTime(),
                this.isSmallNews,
                this.locale
        );
    }
} 