package com.imstargg.core.domain.statistics.brawler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.imstargg.core.domain.statistics.BattleEventResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerPairResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerResultStatistics;
import com.imstargg.core.support.ObjectMapperHelper;
import com.imstargg.storage.db.core.cache.CacheKeyBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class BrawlerStatisticsCache {

    private static final Duration TTL = Duration.ofHours(2);
    private static final String VERSION = "v2";
    private static final String EVENT_CACHE_KEY = "event";
    private static final String BRAWLER_ID_CACHE_KEY = "brawler-id";
    private static final String TIER_RANGE_CACHE_KEY = "tier-range";
    private static final String START_DATE_CACHE_KEY = "start-date";
    private static final String END_DATE_CACHE_KEY = "end-date";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapperHelper objectMapper;

    public BrawlerStatisticsCache(
            StringRedisTemplate redisTemplate,
            ObjectMapperHelper objectMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public List<BrawlerResultStatistics> get(
            BrawlerResultStatisticsParam param,
            Function<BrawlerResultStatisticsParam, List<BrawlerResultStatistics>> mappingFunction
    ) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<List<BrawlerResultStatistics>>() {
        })).orElseGet(() -> {
            List<BrawlerResultStatistics> statistics = mappingFunction.apply(param);
            redisTemplate.opsForValue()
                    .set(key(param), objectMapper.write(statistics), TTL);
            return statistics;
        });
    }

    private String key(BrawlerResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-result-stats", VERSION)
                .add(START_DATE_CACHE_KEY).add(param.startDate())
                .add(END_DATE_CACHE_KEY).add(param.endDate())
                .add(TIER_RANGE_CACHE_KEY).add(param.tierRange().value())
                .build();
    }

    public List<BrawlerPairResultStatistics> get(
            BrawlerPairResultStatisticsParam param,
            Function<BrawlerPairResultStatisticsParam, List<BrawlerPairResultStatistics>> mappingFunction
    ) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<List<BrawlerPairResultStatistics>>() {
        })).orElseGet(() -> {
            List<BrawlerPairResultStatistics> statistics = mappingFunction.apply(param);
            redisTemplate.opsForValue()
                    .set(key(param), objectMapper.write(statistics), TTL);
            return statistics;
        });
    }

    private String key(BrawlerPairResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-pair-result-stats", VERSION)
                .add(BRAWLER_ID_CACHE_KEY).add(param.brawlerId().value())
                .add(START_DATE_CACHE_KEY).add(param.startDate())
                .add(END_DATE_CACHE_KEY).add(param.endDate())
                .add(TIER_RANGE_CACHE_KEY).add(param.tierRange().value())
                .build();
    }

    public List<BrawlerPairResultStatistics> get(
            BrawlerEnemyResultStatisticsParam param,
            Function<BrawlerEnemyResultStatisticsParam, List<BrawlerPairResultStatistics>> mappingFunction
    ) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<List<BrawlerPairResultStatistics>>() {
        })).orElseGet(() -> {
            List<BrawlerPairResultStatistics> statistics = mappingFunction.apply(param);
            redisTemplate.opsForValue()
                    .set(key(param), objectMapper.write(statistics), TTL);
            return statistics;
        });
    }

    private String key(BrawlerEnemyResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-enemy-result-stats", VERSION)
                .add(BRAWLER_ID_CACHE_KEY).add(param.brawlerId().value())
                .add(START_DATE_CACHE_KEY).add(param.startDate())
                .add(END_DATE_CACHE_KEY).add(param.endDate())
                .add(TIER_RANGE_CACHE_KEY).add(param.tierRange().value())
                .build();
    }

    public List<BattleEventResultStatistics> get(
            BrawlerBattleEventResultStatisticsParam param,
            Function<BrawlerBattleEventResultStatisticsParam, List<BattleEventResultStatistics>> mappingFunction
    ) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<List<BattleEventResultStatistics>>() {
        })).orElseGet(() -> {
            List<BattleEventResultStatistics> statistics = mappingFunction.apply(param);
            redisTemplate.opsForValue()
                    .set(key(param), objectMapper.write(statistics), TTL);
            return statistics;
        });
    }

    private String key(BrawlerBattleEventResultStatisticsParam param) {
        return new CacheKeyBuilder("brawler-event-result-stats", VERSION)
                .add(BRAWLER_ID_CACHE_KEY).add(param.brawlerId().value())
                .add(START_DATE_CACHE_KEY).add(param.startDate())
                .add(END_DATE_CACHE_KEY).add(param.endDate())
                .add(TIER_RANGE_CACHE_KEY).add(param.tierRange().value())
                .build();
    }

}
