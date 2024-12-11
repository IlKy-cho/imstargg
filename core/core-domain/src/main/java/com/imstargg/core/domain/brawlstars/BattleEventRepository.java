package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.Message;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.core.enums.Language;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.brawlstars.BattleEventEntity;
import com.imstargg.storage.db.core.brawlstars.BattleEventJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleMapEntity;
import com.imstargg.storage.db.core.brawlstars.BattleMapJpaRepository;
import jakarta.annotation.Nullable;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@CacheConfig(cacheNames = "battle-events")
public class BattleEventRepository {

    private final BattleEventJpaRepository battleEventJpaRepository;
    private final BattleMapJpaRepository battleMapJpaRepository;
    private final MessageRepository messageRepository;

    public BattleEventRepository(
            BattleEventJpaRepository battleEventJpaRepository,
            BattleMapJpaRepository battleMapJpaRepository,
            MessageRepository messageRepository
    ) {
        this.battleEventJpaRepository = battleEventJpaRepository;
        this.battleMapJpaRepository = battleMapJpaRepository;
        this.messageRepository = messageRepository;
    }

    @Cacheable(key = "#language.name() + ':' + #id.value()")
    public PlayerBattleEvent getPlayerBattleEvent(@Nullable BrawlStarsId id, Language language) {
        if (id == null) {
            return PlayerBattleEvent.UNKNOWN;
        }
        Optional<BattleEventEntity> eventEntityOpt = battleEventJpaRepository.findByBrawlStarsId(id.value());
        if (eventEntityOpt.isEmpty()) {
            return PlayerBattleEvent.UNKNOWN;
        }

        BattleEventEntity eventEntity = eventEntityOpt.get();
        BattleMapEntity battleMapEntity = battleMapJpaRepository.findById(eventEntity.getMapId())
                .orElseThrow(() -> new CoreException("맵이 존재하지 않습니다. " +
                        "eventId: " + eventEntity.getId() + ", mapId: " + eventEntity.getMapId()));
        Message battleMapName = messageRepository.get(battleMapEntity.getNameMessageCode(), language);
        return new PlayerBattleEvent(
                new BrawlStarsId(eventEntity.getId()),
                eventEntity.getMode(),
                new BattleMap(
                        battleMapEntity.getCode(),
                        battleMapName.content()
                )
        );
    }
}
