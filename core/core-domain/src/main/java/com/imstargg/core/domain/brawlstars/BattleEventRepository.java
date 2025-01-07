package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.Message;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.BrawlStarsImageType;
import com.imstargg.core.enums.Language;
import com.imstargg.core.enums.NameMessageCodes;
import com.imstargg.storage.db.core.BattleEntityEvent;
import com.imstargg.storage.db.core.BattleJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageJpaRepository;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class BattleEventRepository {

    private final BattleJpaRepository battleJpaRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;

    public BattleEventRepository(
            BattleJpaRepository battleJpaRepository,
            BrawlStarsImageJpaRepository brawlStarsImageJpaRepository,
            MessageRepository messageRepository
    ) {
        this.battleJpaRepository = battleJpaRepository;
        this.brawlStarsImageJpaRepository = brawlStarsImageJpaRepository;
        this.messageRepository = messageRepository;
    }

    public List<BrawlStarsId> findAllEventIds(@Nullable LocalDate date) {
        return battleJpaRepository
                .findAllDistinctEventBrawlStarsIdsByBattleTypeInAndGreaterThanEqualBattleTime(
                        BattleType.regularTypes(),
                        date != null ? date.atStartOfDay() : null
                )
                .stream()
                .map(BrawlStarsId::new)
                .toList();
    }

    public Optional<BattleEvent> find(@Nullable BrawlStarsId id, Language language) {
        if (id == null) {
            return Optional.empty();
        }

        return battleJpaRepository.findLatestBattleByEventBrawlStarsIdAndBattleTypeIn(id.value(), BattleType.regularTypes())
                .map(battleEntity -> new BattleEvent(
                        id,
                        BattleEventMode.find(battleEntity.getEvent().getMode()),
                        new BattleEventMap(
                                battleEventMapName(battleEntity.getEvent(), language),
                                brawlStarsImageJpaRepository.findByCode(BrawlStarsImageType.BATTLE_MAP.code(id.value()))
                                        .map(BrawlStarsImageEntity::getUrl)
                                        .orElse(null)
                        ),
                        battleEntity.getBattleTime()
                ));
    }

    private String battleEventMapName(BattleEntityEvent event, Language language) {
        if (event.getMap() == null) {
            return "";
        }

        return messageRepository.getCollection(NameMessageCodes.BATTLE_MAP.code(event.getMap()))
                .find(language)
                .map(Message::content)
                .orElse(event.getMap());
    }
}
