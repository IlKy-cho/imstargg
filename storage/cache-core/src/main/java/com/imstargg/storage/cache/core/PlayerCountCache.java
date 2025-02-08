package com.imstargg.storage.cache.core;

import jakarta.annotation.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlayerCountCache {

    private static final String PLAYER_KEY = new CacheKeyBuilder("count", "v1")
            .add("player")
            .build();

    private final StringRedisTemplate redisTemplate;

    public PlayerCountCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Nullable
    public Long get() {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(PLAYER_KEY)
        ).map(Long::parseLong).orElse(null);
    }

    public void put(long count) {
        redisTemplate.opsForValue().set(PLAYER_KEY, String.valueOf(count));
    }

}
