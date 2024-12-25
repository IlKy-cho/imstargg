package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.config.CacheNames;
import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.Language;
import jakarta.annotation.Nullable;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@CacheConfig(cacheNames = CacheNames.BRAWLER)
public class BrawlerRepositoryWithCache {

    private final BrawlerRepository brawlerRepository;

    public BrawlerRepositoryWithCache(
            BrawlerRepository brawlerRepository
    ) {
        this.brawlerRepository = brawlerRepository;
    }

    @Cacheable(key = "'brawlers:v1:' + #language.name() + ':' + #id.value()")
    public Optional<Brawler> find(@Nullable BrawlStarsId id, Language language) {
        return brawlerRepository.find(id, language);
    }
}
