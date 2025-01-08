package com.imstargg.core.domain.brawlstars;

import com.imstargg.client.brawlstars.news.BrawlStarsNewsArchiveResponse;
import com.imstargg.client.brawlstars.news.BrawlStarsNewsClient;
import com.imstargg.client.brawlstars.news.BrawlStarsNewsClientException;
import com.imstargg.core.config.CacheNames;
import com.imstargg.core.domain.Slice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.OptionalInt;

@Component
@CacheConfig(cacheNames = CacheNames.BRAWLSTARS_NEWS)
public class BrawlStarsNewsReaderWithCache {

    private static final Logger log = LoggerFactory.getLogger(BrawlStarsNewsReaderWithCache.class);

    private final BrawlStarsNewsClient brawlStarsNewsClient;

    public BrawlStarsNewsReaderWithCache(BrawlStarsNewsClient brawlStarsNewsClient) {
        this.brawlStarsNewsClient = brawlStarsNewsClient;
    }

    @Cacheable(key = "'news:v1:' + #pageParam.language().name() + ':' + #pageParam.page()")
    public Slice<BrawlStarsNews> read(BrawlStarsNewsPageParam pageParam) {
        try {
            BrawlStarsNewsArchiveResponse newsArchiveResponse = brawlStarsNewsClient
                    .getNewsArchive(pageParam.language().getCode(), pageParam.page());

            OptionalInt maxPageNumberOpt = newsArchiveResponse.pageNumbers()
                    .stream()
                    .mapToInt(Integer::intValue)
                    .max();
            boolean hasNext = maxPageNumberOpt.isPresent() && maxPageNumberOpt.getAsInt() > pageParam.page();
            return new Slice<>(
                    newsArchiveResponse.articles().stream()
                            .map(newsResponse -> new BrawlStarsNews(
                                    newsResponse.title(),
                                    newsResponse.linkUrl(),
                                    newsResponse.publishDate()
                            ))
                            .toList(),
                    hasNext
            );
        } catch (BrawlStarsNewsClientException.NotFound e) {
            return Slice.empty();
        }
    }
}
