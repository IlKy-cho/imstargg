package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.Message;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
import com.imstargg.core.enums.Language;
import com.imstargg.storage.db.core.MessageCodes;
import com.imstargg.storage.db.core.BattleJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleEventEntity;
import com.imstargg.storage.db.core.brawlstars.BattleEventJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageJpaRepository;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BattleEventRepository {

    private final Clock clock;
    private final BattleJpaRepository battleJpaRepository;
    private final BattleEventJpaRepository battleEventJpaRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;
    private final BattleEventCache battleEventCache;

    public BattleEventRepository(
            Clock clock,
            BattleJpaRepository battleJpaRepository,
            BattleEventJpaRepository battleEventJpaRepository,
            BrawlStarsImageJpaRepository brawlStarsImageJpaRepository,
            MessageRepository messageRepository,
            BattleEventCache battleEventCache
    ) {
        this.clock = clock;
        this.battleJpaRepository = battleJpaRepository;
        this.battleEventJpaRepository = battleEventJpaRepository;
        this.brawlStarsImageJpaRepository = brawlStarsImageJpaRepository;
        this.messageRepository = messageRepository;
        this.battleEventCache = battleEventCache;
    }

    public List<BrawlStarsId> findAllEventIds(@Nullable LocalDate date) {
        return battleJpaRepository
                .findAllDistinctEventBrawlStarsIdsByBattleTypeInAndGreaterThanEqualBattleTime(
                        BattleType.regularTypes(),
                        date != null ? date.atStartOfDay().atZone(clock.getZone()).toOffsetDateTime() : null
                )
                .stream()
                .map(BrawlStarsId::new)
                .toList();
    }

    public List<BattleEvent> findAllEvents(Language language, @Nullable LocalDate date) {
        return findAllEvents(findAllEventIds(date), language);
    }

    public List<BattleEvent> findAllEvents(List<BrawlStarsId> eventIds, Language language) {
        Map<BrawlStarsId, BattleEvent> idToEventInCache = eventIds.stream()
                .map(id -> battleEventCache.find(id, language))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(BattleEvent::id, Function.identity()));

        List<Long> eventBrawlStarsIdsNotInCache = eventIds.stream()
                .map(BrawlStarsId::value)
                .toList();

        List<BattleEventEntity> eventEntities = battleEventJpaRepository.findAllByBrawlStarsIdIn(eventBrawlStarsIdsNotInCache);
        List<String> nameMessageCodes = eventEntities.stream()
                .map(BattleEventEntity::getMapBrawlStarsName)
                .filter(Objects::nonNull)
                .distinct()
                .map(MessageCodes.BATTLE_MAP_NAME::code)
                .toList();
        Map<String, MessageCollection> codeToMessage = messageRepository.getCollectionList(nameMessageCodes).stream()
                .collect(Collectors.toMap(MessageCollection::getCode, Function.identity()));
        Map<String, BrawlStarsImageEntity> codeToImage = brawlStarsImageJpaRepository.findAllByCodeIn(
                        eventBrawlStarsIdsNotInCache.stream()
                                .map(BrawlStarsImageType.BATTLE_MAP::code)
                                .toList()
                ).stream()
                .collect(Collectors.toMap(BrawlStarsImageEntity::getCode, Function.identity()));

        Map<BrawlStarsId, BattleEvent> idToEventNotInCache = eventEntities.stream()
                .map(eventEntity -> new BattleEvent(
                        new BrawlStarsId(eventEntity.getBrawlStarsId()),
                        BattleEventMode.find(eventEntity.getMode()),
                        new BattleEventMap(
                                eventEntity.getMapBrawlStarsName() == null ? null :
                                        codeToMessage.get(MessageCodes.BATTLE_MAP_NAME
                                                        .code(eventEntity.getMapBrawlStarsName()))
                                                .find(language)
                                                .map(Message::content)
                                                .orElse(eventEntity.getMapBrawlStarsName()),
                                Optional.ofNullable(
                                                codeToImage.get(BrawlStarsImageType.BATTLE_MAP
                                                        .code(eventEntity.getBrawlStarsId()))
                                        ).map(BrawlStarsImageEntity::getStoredName)
                                        .orElse(null)
                        ),
                        eventEntity.getLatestBattleTime() != null
                                ? eventEntity.getLatestBattleTime().toLocalDateTime()
                                : null
                )).map(event -> {
                    battleEventCache.set(event.id(), language, event);
                    return event;
                }).collect(Collectors.toMap(BattleEvent::id, Function.identity()));

        return eventIds.stream()
                .filter(id -> idToEventInCache.containsKey(id) || idToEventNotInCache.containsKey(id))
                .map(id -> idToEventInCache.getOrDefault(id, idToEventNotInCache.get(id)))
                .toList();
    }

    public Optional<BattleEvent> find(@Nullable BrawlStarsId id, Language language) {
        if (id == null) {
            return Optional.empty();
        }

        return battleEventCache.find(id, language).or(() -> battleEventJpaRepository.findByBrawlStarsId(id.value())
                .map(eventEntity ->
                        new BattleEvent(
                                id,
                                BattleEventMode.find(eventEntity.getMode()),
                                new BattleEventMap(
                                        eventEntity.getMapBrawlStarsName() == null ? null :
                                                messageRepository.getCollection(MessageCodes.BATTLE_MAP_NAME
                                                                .code(eventEntity.getMapBrawlStarsName()))
                                                        .find(language)
                                                        .map(Message::content)
                                                        .orElse(eventEntity.getMapBrawlStarsName()),
                                        brawlStarsImageJpaRepository.findByCode(
                                                        BrawlStarsImageType.BATTLE_MAP
                                                                .code(eventEntity.getBrawlStarsId())
                                                ).map(BrawlStarsImageEntity::getStoredName)
                                                .orElse(null)
                                ),
                                eventEntity.getLatestBattleTime() != null
                                        ? eventEntity.getLatestBattleTime().toLocalDateTime()
                                        : null
                        )
                ).map(event -> {
                    battleEventCache.set(id, language, event);
                    return event;
                })
        );
    }
}
