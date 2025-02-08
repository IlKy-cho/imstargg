package com.imstargg.core.domain.brawlstars;

import com.fasterxml.jackson.core.type.TypeReference;
import com.imstargg.core.enums.Language;
import com.imstargg.core.support.ObjectMapperHelper;
import com.imstargg.storage.db.core.cache.CacheKeyBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Component
public class BrawlStarsNewsCache {

    private static final TypeReference<List<BrawlStarsNews>> typeReference = new TypeReference<>() {
    };
    private static final Duration TTL = Duration.ofHours(6);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapperHelper objectMapper;

    public BrawlStarsNewsCache(StringRedisTemplate redisTemplate, ObjectMapperHelper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    private static String key(Language language) {
        return new CacheKeyBuilder("news", "v1")
                .add("lang").add(language.name())
                .build();
    }

    public Optional<List<BrawlStarsNews>> find(Language language) {
        return Optional.ofNullable(
                        redisTemplate.opsForValue().get(key(language))
                ).map(value -> objectMapper.read(value, typeReference));
    }

    public void set(Language language, List<BrawlStarsNews> news) {
        redisTemplate.opsForValue().set(key(language), objectMapper.write(news), TTL);
    }
}
