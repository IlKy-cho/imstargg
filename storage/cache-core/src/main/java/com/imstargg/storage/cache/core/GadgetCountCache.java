package com.imstargg.storage.cache.core;

import jakarta.annotation.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GadgetCountCache {

    private final StringRedisTemplate redisTemplate;

    public GadgetCountCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Nullable
    public Long get(long brawlStarsId) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(new GadgetCountCacheKey(brawlStarsId).key())
        ).map(Long::parseLong).orElse(null);
    }

    public void put(long brawlStarsId, long count) {
        redisTemplate.opsForValue().set(new GadgetCountCacheKey(brawlStarsId).key(), String.valueOf(count));
    }
}
