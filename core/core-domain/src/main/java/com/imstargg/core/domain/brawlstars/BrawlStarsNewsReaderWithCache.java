package com.imstargg.core.domain.brawlstars;

import com.imstargg.client.brawlstars.news.BrawlStarsNewsClient;
import com.imstargg.core.enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrawlStarsNewsReaderWithCache {

    private static final Logger log = LoggerFactory.getLogger(BrawlStarsNewsReaderWithCache.class);

    private final BrawlStarsNewsClient brawlStarsNewsClient;
    private final BrawlStarsNewsCache cache;

    public BrawlStarsNewsReaderWithCache(BrawlStarsNewsClient brawlStarsNewsClient, BrawlStarsNewsCache cache) {
        this.brawlStarsNewsClient = brawlStarsNewsClient;
        this.cache = cache;
    }

    public List<BrawlStarsNews> read(Language language) {
        return cache.find(language)
                .orElseGet(() -> {
                    try {
                        List<BrawlStarsNews> news = brawlStarsNewsClient
                                .getNewsArchive(language.getCode(), 1).articles().stream()
                                .map(newsResponse -> new BrawlStarsNews(
                                        newsResponse.title(),
                                        newsResponse.linkUrl(),
                                        newsResponse.publishDate()
                                ))
                                .toList();
                        cache.set(language, news);
                        return news;
                    } catch (Exception e) {
                        log.error("Failed to read news", e);
                        return List.of();
                    }
                });
    }
}
