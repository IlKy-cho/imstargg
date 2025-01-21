package com.imstargg.client.brawlstars.news;

import java.util.List;

public record ArchivePageDataDto(Props props) {
    public record Props(PageProps pageProps) {
    }

    public record PageProps(
            int currentPage,
            List<Integer> pageNumbers,
            List<BrawlStarsNewsArticleResponse> articles
    ) {
        public BrawlStarsNewsArchiveResponse toResponse() {
            return new BrawlStarsNewsArchiveResponse(
                    this.currentPage,
                    this.pageNumbers,
                    this.articles
            );
        }
    }
} 