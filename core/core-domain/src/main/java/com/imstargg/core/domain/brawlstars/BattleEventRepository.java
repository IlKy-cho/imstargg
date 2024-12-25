package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.Message;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.core.enums.BrawlStarsImageType;
import com.imstargg.core.enums.Language;
import com.imstargg.core.error.CoreException;
import com.imstargg.storage.db.core.brawlstars.BattleEventJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleMapEntity;
import com.imstargg.storage.db.core.brawlstars.BattleMapJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageJpaRepository;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BattleEventRepository {

    private final BattleEventJpaRepository battleEventJpaRepository;
    private final BattleMapJpaRepository battleMapJpaRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;

    public BattleEventRepository(
            BattleEventJpaRepository battleEventJpaRepository,
            BattleMapJpaRepository battleMapJpaRepository,
            BrawlStarsImageJpaRepository brawlStarsImageJpaRepository,
            MessageRepository messageRepository
    ) {
        this.battleEventJpaRepository = battleEventJpaRepository;
        this.battleMapJpaRepository = battleMapJpaRepository;
        this.brawlStarsImageJpaRepository = brawlStarsImageJpaRepository;
        this.messageRepository = messageRepository;
    }

    public Optional<BattleEvent> find(@Nullable BrawlStarsId id, Language language) {
        if (id == null) {
            return Optional.empty();
        }

        return battleEventJpaRepository.findByBrawlStarsId(id.value())
                .map(eventEntity -> {
                    BattleMapEntity battleMapEntity = battleMapJpaRepository.findById(eventEntity.getMapId())
                            .orElseThrow(() -> new CoreException("맵이 존재하지 않습니다. " +
                                    "eventId: " + eventEntity.getId() + ", mapId: " + eventEntity.getMapId()));
                    Message battleMapName = messageRepository.get(battleMapEntity.getNameMessageCode(), language);

                    return new BattleEvent(
                            new BrawlStarsId(eventEntity.getId()),
                            eventEntity.getMode(),
                            new BattleMap(
                                    battleMapEntity.getCode(),
                                    battleMapName.content(),
                                    brawlStarsImageJpaRepository.findByCode(BrawlStarsImageType.BATTLE_MAP.code(
                                            battleMapEntity.getCode()
                                    )).map(BrawlStarsImageEntity::getUrl).orElse(null)
                            )
                    );
                });
    }
}
