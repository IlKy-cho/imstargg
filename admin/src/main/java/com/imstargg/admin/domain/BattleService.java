package com.imstargg.admin.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.imstargg.admin.support.error.AdminErrorKind;
import com.imstargg.admin.support.error.AdminException;
import com.imstargg.core.enums.BattleType;
import com.imstargg.core.enums.Language;
import com.imstargg.storage.db.core.MessageCodes;
import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
import com.imstargg.storage.db.core.player.BattleEntity;
import com.imstargg.storage.db.core.player.BattleJpaRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class BattleService {

    private final Cache<Long, Optional<BattleEntity>> eventBrawlStarsIdToLastBattleCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(30))
            .build();
    private final BrawlStarsImageUploader brawlStarsImageUploader;
    private final BattleJpaRepository battleJpaRepository;
    private final BattleEventCollectionJpaRepository battleEventCollectionJpaRepository;
    private final BrawlStarsImageCollectionJpaRepository brawlStarsImageRepository;
    private final MessageCollectionJpaRepository messageRepository;

    public BattleService(
            BrawlStarsImageUploader brawlStarsImageUploader,
            BattleJpaRepository battleMapRepository,
            BattleEventCollectionJpaRepository battleEventCollectionJpaRepository,
            BrawlStarsImageCollectionJpaRepository brawlStarsImageRepository,
            MessageCollectionJpaRepository messageRepository
    ) {
        this.brawlStarsImageUploader = brawlStarsImageUploader;
        this.battleJpaRepository = battleMapRepository;
        this.battleEventCollectionJpaRepository = battleEventCollectionJpaRepository;
        this.brawlStarsImageRepository = brawlStarsImageRepository;
        this.messageRepository = messageRepository;
    }


    public List<BattleEvent> getEventList() {
        List<BattleEventCollectionEntity> events = battleEventCollectionJpaRepository.findAll();
        Map<Long, BattleEntity> eventIdToLatestBattle = findLatestBattle(events)
                .stream().collect(toMap(battle -> battle.getEvent().getBrawlStarsId(), Function.identity()));

        Map<String, List<MessageCollectionEntity>> mapCodeToNames = messageRepository.findAllByCodeIn(
                        events.stream()
                                .map(BattleEventCollectionEntity::getMapBrawlStarsName)
                                .filter(Objects::nonNull)
                                .map(MessageCodes.BATTLE_MAP_NAME::code)
                                .toList()
                ).stream()
                .collect(Collectors.groupingBy(MessageCollectionEntity::getCode));

        Map<String, BrawlStarsImageCollectionEntity> mapImageCodeToImage = brawlStarsImageRepository
                .findAllByType(BrawlStarsImageType.BATTLE_MAP)
                .stream()
                .collect(toMap(BrawlStarsImageCollectionEntity::getCode, Function.identity()));

        return events.stream()
                .map(event -> new BattleEvent(
                        event,
                        new BattleEventMap(
                                Optional.ofNullable(event.getMapBrawlStarsName())
                                        .map(MessageCodes.BATTLE_MAP_NAME::code)
                                        .map(mapCodeToNames::get)
                                        .orElse(List.of()),
                                mapImageCodeToImage.get(BrawlStarsImageType.BATTLE_MAP.code(event.getBrawlStarsId()))
                        ),
                        Optional.ofNullable(eventIdToLatestBattle.get(event.getBrawlStarsId()))
                                .map(BattleEntity::getMode)
                                .orElse(null),
                        Optional.ofNullable(eventIdToLatestBattle.get(event.getBrawlStarsId()))
                                .map(BattleEntity::getBattleTime)
                                .orElse(null),
                        event.isSoloRanked()
                )).toList();
    }

    private List<BattleEntity> findLatestBattle(List<BattleEventCollectionEntity> events) {
        return events
                .stream()
                .map(BattleEventCollectionEntity::getBrawlStarsId)
                .map(eventBrawlStarsId ->
                        eventBrawlStarsIdToLastBattleCache.get(eventBrawlStarsId, key ->
                                battleJpaRepository
                                        .findLatestBattleByEventBrawlStarsIdAndBattleTypeIn(
                                                eventBrawlStarsId, BattleType.regularTypes())
                        )
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Transactional
    public void updateBattleEvent(long eventBrawlStarsId, BattleEventUpdate update) {
        update.map().names().validate();
        BattleEventCollectionEntity eventEntity = battleEventCollectionJpaRepository.findByBrawlStarsId(eventBrawlStarsId)
                .orElseThrow(() -> new AdminException(
                        AdminErrorKind.NOT_FOUND, "해당 이벤트를 찾을 수 없습니다. 이벤트 ID: " + eventBrawlStarsId));
        if (eventEntity.getMapBrawlStarsName() == null) {
            throw new AdminException(AdminErrorKind.VALIDATION_FAILED, "맵 이름이 없는 이벤트는 수정할 수 없습니다.");
        }
        Map<Language, MessageCollectionEntity> langToMessageEntity = messageRepository
                .findAllByCode(MessageCodes.BATTLE_MAP_NAME.code(eventEntity.getMapBrawlStarsName()))
                .stream()
                .collect(Collectors.toMap(MessageCollectionEntity::getLang, Function.identity()));

        messageRepository.saveAll(
                update.map().names().messages().entrySet().stream()
                        .map(entry -> Optional.ofNullable(langToMessageEntity.get(entry.getKey()))
                                .map(entity -> {
                                    entity.update(entry.getValue());
                                    return entity;
                                })
                                .orElseGet(() -> new MessageCollectionEntity(
                                        MessageCodes.BATTLE_MAP_NAME.code(eventEntity.getMapBrawlStarsName()),
                                        entry.getKey(),
                                        entry.getValue()
                                ))
                        ).toList()
        );
    }

    public void uploadMapImage(long eventBrawlStarsId, Resource resource) {
        brawlStarsImageUploader.uploadMap(eventBrawlStarsId, resource);
    }

}
