package com.imstargg.storage.cache.core;

import com.imstargg.storage.db.core.cache.GearCountCacheKey;
import jakarta.annotation.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GearCountCache {

    private final StringRedisTemplate redisTemplate;

    public GearCountCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Nullable
    Long get(long brawlerBrawlStarsId, long gearBrawlStarsId) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(new GearCountCacheKey(brawlerBrawlStarsId, gearBrawlStarsId).key())
        ).map(Long::parseLong).orElse(null);
    }

    public void put(long brawlerBrawlStarsId, long gearBrawlStarsId, long count) {
        redisTemplate.opsForValue().set(
                new GearCountCacheKey(brawlerBrawlStarsId, gearBrawlStarsId).key(), String.valueOf(count)
        );
    }

}
