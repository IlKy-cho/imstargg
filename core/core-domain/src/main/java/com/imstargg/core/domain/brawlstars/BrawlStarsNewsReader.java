package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.config.CacheNames;
import com.imstargg.core.domain.Slice;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = CacheNames.BRAWL_STARS_NEWS)
public class BrawlStarsNewsReader {

    private final BrawlStarsNewsRepository brawlStarsNewsRepository;

    public BrawlStarsNewsReader(BrawlStarsNewsRepository brawlStarsNewsRepository) {
        this.brawlStarsNewsRepository = brawlStarsNewsRepository;
    }

    @Cacheable(key = "'news:v1:' + #pageParam.language() + ':' + #pageParam.page()")
    public Slice<BrawlStarsNews> read(BrawlStarsNewsPageParam pageParam) {
        return brawlStarsNewsRepository.find(pageParam.language(), pageParam);
    }
}
