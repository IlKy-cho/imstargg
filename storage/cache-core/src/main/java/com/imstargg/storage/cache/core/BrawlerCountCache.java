package com.imstargg.storage.cache.core;

import jakarta.annotation.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BrawlerCountCache {

    private final StringRedisTemplate redisTemplate;

    public BrawlerCountCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Nullable
    public Long get(long brawlStarsId) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(new BrawlerCountCacheKey(brawlStarsId).key())
        ).map(Long::parseLong).orElse(null);
    }

    public void put(long brawlStarsId, long count) {
        redisTemplate.opsForValue().set(new BrawlerCountCacheKey(brawlStarsId).key(), String.valueOf(count));
    }
}
