package com.imstargg.client.brawlstars.news;

import java.util.List;

public record ArchivePageDataDto(Props props) {
    public record Props(PageProps pageProps) {
    }

    public record PageProps(
            int currentPage,
            List<Integer> pageNumbers,
            List<BrawlStarsNewsArticleResponseDto> articles
    ) {
        public BrawlStarsNewsArchiveResponse toResponse() {
            return new BrawlStarsNewsArchiveResponse(
                    this.currentPage,
                    this.pageNumbers,
                    this.articles.stream()
                            .map(BrawlStarsNewsArticleResponseDto::toResponse)
                            .toList()
            );
        }
    }
} 