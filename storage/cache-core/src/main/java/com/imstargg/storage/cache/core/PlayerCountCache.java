package com.imstargg.storage.cache.core;

import com.imstargg.storage.db.core.cache.PlayerCountCacheKey;
import jakarta.annotation.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlayerCountCache {

    private final StringRedisTemplate redisTemplate;

    public PlayerCountCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Nullable
    public Long get() {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(PlayerCountCacheKey.KEY)
        ).map(Long::parseLong).orElse(null);
    }

    public void put(long count) {
        redisTemplate.opsForValue().set(PlayerCountCacheKey.KEY, String.valueOf(count));
    }

}
