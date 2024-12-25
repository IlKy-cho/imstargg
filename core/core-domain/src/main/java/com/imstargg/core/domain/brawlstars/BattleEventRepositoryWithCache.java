package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.config.CacheNames;
import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.Language;
import jakarta.annotation.Nullable;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@CacheConfig(cacheNames = CacheNames.BATTLE_EVENT)
public class BattleEventRepositoryWithCache {

    private final BattleEventRepository battleEventRepository;

    public BattleEventRepositoryWithCache(
            BattleEventRepository battleEventRepository
    ) {
        this.battleEventRepository = battleEventRepository;
    }

    @Cacheable(key = "'battle-events:v1:' + #language.name() + ':' + #id.value()")
    public Optional<BattleEvent> find(@Nullable BrawlStarsId id, Language language) {
        return battleEventRepository.find(id, language);
    }
}
