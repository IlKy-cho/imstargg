package com.imstargg.storage.cache.core;

import com.imstargg.storage.db.core.cache.StarPowerCountCacheKey;
import jakarta.annotation.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StarPowerCountCache {

    private final StringRedisTemplate redisTemplate;

    public StarPowerCountCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Nullable
    public Long get(long brawlStarsId) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(new StarPowerCountCacheKey(brawlStarsId).key())
        ).map(Long::parseLong).orElse(null);
    }

    public void put(long brawlStarsId, long count) {
        redisTemplate.opsForValue().set(new StarPowerCountCacheKey(brawlStarsId).key(), String.valueOf(count));
    }

}
