package com.imstargg.client.brawlstars.news;

import java.net.URI;
import java.util.List;

public record BrawlStarsNewsArchiveResponse(
        int currentPage,
        List<Integer> pageNumbers,
        List<BrawlStarsNewsArticleResponse> articles
) {
    public BrawlStarsNewsArchiveResponse toArticleFullLinkUrl(URI baseUrl) {
        return new BrawlStarsNewsArchiveResponse(
                this.currentPage,
                this.pageNumbers,
                this.articles.stream()
                        .map(article -> article.toFullLinkUrl(baseUrl))
                        .toList()
        );
    }
}
