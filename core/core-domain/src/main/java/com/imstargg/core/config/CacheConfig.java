package com.imstargg.core.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(
                Caffeine.newBuilder().expireAfterWrite(Duration.ofHours(1))
        );

        cacheManager.setCacheNames(List.of(
                CacheNames.BATTLE_EVENT,
                CacheNames.BRAWLER,
                CacheNames.BRAWL_STARS_NEWS
        ));

        return cacheManager;
    }
}
