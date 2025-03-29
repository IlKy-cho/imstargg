package com.imstargg.core.domain.statistics.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.imstargg.core.domain.statistics.BrawlerRankStatistics;
import com.imstargg.core.domain.statistics.BrawlerResultStatistics;
import com.imstargg.core.domain.statistics.BrawlerPairRankStatistics;
import com.imstargg.core.domain.statistics.BrawlerPairResultStatistics;
import com.imstargg.core.support.ObjectMapperHelper;
import com.imstargg.storage.db.core.cache.CacheKeyBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class BattleEventStatisticsCache {

    private static final Duration TTL = Duration.ofHours(2);
    private static final String VERSION = "v2";
    private static final String EVENT_CACHE_KEY = "event";
    private static final String BRAWLER_ID_CACHE_KEY = "brawler-id";
    private static final String TIER_RANGE_CACHE_KEY = "tier-range";
    private static final String START_DATE_CACHE_KEY = "start-date";
    private static final String END_DATE_CACHE_KEY = "end-date";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapperHelper objectMapper;

    public BattleEventStatisticsCache(
            StringRedisTemplate redisTemplate,
            ObjectMapperHelper objectMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public List<BrawlerResultStatistics> get(
            BattleEventBrawlerResultStatisticsParam param,
            Function<BattleEventBrawlerResultStatisticsParam, List<BrawlerResultStatistics>> mappingFunction
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

    private String key(BattleEventBrawlerResultStatisticsParam param) {
        return new CacheKeyBuilder("event-brawler-result-stats", VERSION)
                .add(EVENT_CACHE_KEY).add(param.eventId().value())
                .add(START_DATE_CACHE_KEY).add(param.startDate())
                .add(END_DATE_CACHE_KEY).add(param.endDate())
                .add(TIER_RANGE_CACHE_KEY).add(param.tierRange().value())
                .build();
    }

    public List<BrawlerPairResultStatistics> get(
            BattleEventBrawlerPairResultStatisticsParam param,
            Function<BattleEventBrawlerPairResultStatisticsParam, List<BrawlerPairResultStatistics>> mappingFunction
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

    private String key(BattleEventBrawlerPairResultStatisticsParam param) {
        return new CacheKeyBuilder("event-brawlers-result-stats", VERSION)
                .add(EVENT_CACHE_KEY).add(param.eventId().value())
                .add(BRAWLER_ID_CACHE_KEY).add(param.brawlerId().value())
                .add(START_DATE_CACHE_KEY).add(param.startDate())
                .add(END_DATE_CACHE_KEY).add(param.endDate())
                .add(TIER_RANGE_CACHE_KEY).add(param.tierRange().value())
                .build();
    }

    public List<BrawlerPairResultStatistics> get(
            BattleEventBrawlerEnemyResultStatisticsParam param,
            Function<BattleEventBrawlerEnemyResultStatisticsParam, List<BrawlerPairResultStatistics>> mappingFunction
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

    private String key(BattleEventBrawlerEnemyResultStatisticsParam param) {
        return new CacheKeyBuilder("event-brawler-enemy-result-stats", VERSION)
                .add(EVENT_CACHE_KEY).add(param.eventId().value())
                .add(BRAWLER_ID_CACHE_KEY).add(param.brawlerId().value())
                .add(START_DATE_CACHE_KEY).add(param.startDate())
                .add(END_DATE_CACHE_KEY).add(param.endDate())
                .add(TIER_RANGE_CACHE_KEY).add(param.tierRange().value())
                .build();
    }

    public List<BrawlerRankStatistics> get(
            BattleEventBrawlerRankStatisticsParam param,
            Function<BattleEventBrawlerRankStatisticsParam, List<BrawlerRankStatistics>> mappingFunction
    ) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<List<BrawlerRankStatistics>>() {
        })).orElseGet(() -> {
            List<BrawlerRankStatistics> statistics = mappingFunction.apply(param);
            redisTemplate.opsForValue()
                    .set(key(param), objectMapper.write(statistics), TTL);
            return statistics;
        });
    }

    private String key(BattleEventBrawlerRankStatisticsParam param) {
        return new CacheKeyBuilder("event-brawler-rank-stats", VERSION)
                .add(EVENT_CACHE_KEY).add(param.eventId().value())
                .add(START_DATE_CACHE_KEY).add(param.startDate())
                .add(END_DATE_CACHE_KEY).add(param.endDate())
                .add(TIER_RANGE_CACHE_KEY).add(param.trophyRange())
                .build();
    }

    public List<BrawlerPairRankStatistics> get(
            BattleEventBrawlerPairRankStatisticsParam param,
            Function<BattleEventBrawlerPairRankStatisticsParam, List<BrawlerPairRankStatistics>> mappingFunction
    ) {
        return Optional.ofNullable(
                redisTemplate.opsForValue().get(key(param))
        ).map(value -> objectMapper.read(value, new TypeReference<List<BrawlerPairRankStatistics>>() {
        })).orElseGet(() -> {
            List<BrawlerPairRankStatistics> statistics = mappingFunction.apply(param);
            redisTemplate.opsForValue()
                    .set(key(param), objectMapper.write(statistics), TTL);
            return statistics;
        });
    }

    private String key(BattleEventBrawlerPairRankStatisticsParam param) {
        return new CacheKeyBuilder("event-brawlers-rank-stats", VERSION)
                .add(EVENT_CACHE_KEY).add(param.eventId().value())
                .add(BRAWLER_ID_CACHE_KEY).add(param.brawlerId().value())
                .add(START_DATE_CACHE_KEY).add(param.startDate())
                .add(END_DATE_CACHE_KEY).add(param.endDate())
                .add(TIER_RANGE_CACHE_KEY).add(param.trophyRange())
                .build();
    }

}
