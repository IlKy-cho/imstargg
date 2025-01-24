package com.imstargg.core.domain.brawlstars;

import com.imstargg.client.brawlstars.news.BrawlStarsNewsArchiveResponse;
import com.imstargg.client.brawlstars.news.BrawlStarsNewsClient;
import com.imstargg.core.config.CacheNames;
import com.imstargg.core.enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@CacheConfig(cacheNames = CacheNames.BRAWLSTARS_NEWS)
public class BrawlStarsNewsReaderWithCache {

    private static final Logger log = LoggerFactory.getLogger(BrawlStarsNewsReaderWithCache.class);

    private final BrawlStarsNewsClient brawlStarsNewsClient;

    public BrawlStarsNewsReaderWithCache(BrawlStarsNewsClient brawlStarsNewsClient) {
        this.brawlStarsNewsClient = brawlStarsNewsClient;
    }

    @Cacheable(key = "'news:v1:' + #language")
    public List<BrawlStarsNews> read(Language language) {
        try {
            BrawlStarsNewsArchiveResponse newsArchiveResponse = brawlStarsNewsClient
                    .getNewsArchive(language.getCode(), 1);

            return newsArchiveResponse.articles().stream()
                    .map(newsResponse -> new BrawlStarsNews(
                            newsResponse.title(),
                            newsResponse.linkUrl(),
                            newsResponse.publishDate()
                    ))
                    .toList();
        } catch (Exception e) {
            log.error("Failed to read news", e);
            return List.of();
        }
    }
}
