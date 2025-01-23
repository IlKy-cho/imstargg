package com.imstargg.client.brawlstars;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
class BrawlStarsClientCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .expireAfterWrite(Duration.ofSeconds(15))
                        .maximumSize(1000)
        );
        cacheManager.setCacheNames(List.of(
                BrawlStarsClientCacheNames.BRAWLSTARS_CLIENT
        ));
        return cacheManager;
    }
}
