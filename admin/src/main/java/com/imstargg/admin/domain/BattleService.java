package com.imstargg.admin.domain;

import com.imstargg.admin.support.error.AdminErrorKind;
import com.imstargg.admin.support.error.AdminException;
import com.imstargg.core.enums.BattleEventMode;
import com.imstargg.core.enums.BrawlStarsImageType;
import com.imstargg.storage.db.core.BaseEntity;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleEventCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BattleMapCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BattleMapCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.SeasonBattleEventCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.SeasonBattleEventCollectionJpaRepository;
import com.imstargg.storage.db.core.collection.UnregisteredBattleEventCollectionJpaRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
public class BattleService {

    private final BrawlStarsImageUploader brawlStarsImageUploader;
    private final BattleMapCollectionJpaRepository battleMapRepository;
    private final BattleEventCollectionJpaRepository battleEventRepository;
    private final SeasonBattleEventCollectionJpaRepository seasonedBattleEventRepository;
    private final UnregisteredBattleEventCollectionJpaRepository unregisteredBattleEventRepository;
    private final BrawlStarsImageCollectionJpaRepository brawlStarsImageRepository;
    private final MessageCollectionJpaRepository messageRepository;

    public BattleService(
            BrawlStarsImageUploader brawlStarsImageUploader,
            BattleMapCollectionJpaRepository battleMapRepository,
            BattleEventCollectionJpaRepository battleEventRepository,
            SeasonBattleEventCollectionJpaRepository seasonedBattleEventRepository,
            UnregisteredBattleEventCollectionJpaRepository unregisteredBattleEventRepository,
            BrawlStarsImageCollectionJpaRepository brawlStarsImageRepository,
            MessageCollectionJpaRepository messageRepository
    ) {
        this.brawlStarsImageUploader = brawlStarsImageUploader;
        this.battleMapRepository = battleMapRepository;
        this.battleEventRepository = battleEventRepository;
        this.seasonedBattleEventRepository = seasonedBattleEventRepository;
        this.unregisteredBattleEventRepository = unregisteredBattleEventRepository;
        this.brawlStarsImageRepository = brawlStarsImageRepository;
        this.messageRepository = messageRepository;
    }

    public List<BattleMap> getMapList() {
        List<BattleMapCollectionEntity> maps = battleMapRepository.findAll();
        Map<String, BrawlStarsImageCollectionEntity> codeToImage = brawlStarsImageRepository
                .findAllByType(BrawlStarsImageType.BATTLE_MAP).stream()
                .collect(toMap(BrawlStarsImageCollectionEntity::getCode, Function.identity()));
        Map<String, List<MessageCollectionEntity>> codeToMessages = messageRepository.findAllByCodeIn(maps.stream()
                        .map(BattleMapCollectionEntity::getNameMessageCode).toList())
                .stream()
                .collect(groupingBy(MessageCollectionEntity::getCode));
        Map<Long, List<BattleEventCollectionEntity>> mapIdToEvent = battleEventRepository.findAll().stream()
                .collect(groupingBy(event -> event.getMap().getId()));


        return maps.stream()
                .map(map ->
                        new BattleMap(
                                map,
                                codeToMessages.get(map.getNameMessageCode()),
                                codeToImage.get(BrawlStarsImageType.BATTLE_MAP.code(map.getCode())),
                                mapIdToEvent.getOrDefault(map.getId(), List.of())
                        )
                ).toList();
    }

    @Transactional
    public void registerMap(NewBattleMap newBattleMap) {
        newBattleMap.names().validate();
        BattleMapCollectionEntity battleMap = battleMapRepository.save(new BattleMapCollectionEntity());

        newBattleMap.names().messages().forEach((language, name) -> messageRepository.save(
                new MessageCollectionEntity(battleMap.getNameMessageCode(), language, name)));
    }

    public void uploadMapImage(String mapCode, Resource resource) {
        brawlStarsImageUploader.uploadMap(mapCode, resource);
    }

    public List<BattleEvent> getEventList() {
        List<BattleEventCollectionEntity> events = battleEventRepository.findAll();
        Map<Long, BattleMapCollectionEntity> idToMap = battleMapRepository.findAll().stream()
                .collect(toMap(BattleMapCollectionEntity::getId, Function.identity()));
        List<MessageCollectionEntity> mapNameCodeToMessage = messageRepository.findAllByCodeIn(
                idToMap.values().stream().map(BattleMapCollectionEntity::getNameMessageCode).toList());
        Map<String, BrawlStarsImageCollectionEntity> mapImageCodeToImage = brawlStarsImageRepository
                .findAllByType(BrawlStarsImageType.BATTLE_MAP)
                .stream()
                .collect(toMap(BrawlStarsImageCollectionEntity::getCode, Function.identity()));
        Set<Long> seasonedEventIds = seasonedBattleEventRepository.findAll().stream()
                .filter(BaseEntity::isActive)
                .map(SeasonBattleEventCollectionEntity::getBattleEvent)
                .map(BattleEventCollectionEntity::getId)
                .collect(Collectors.toSet());

        return events.stream()
                .map(event -> new BattleEvent(
                        event,
                        new BattleEventMap(
                                idToMap.get(event.getMap().getId()),
                                mapNameCodeToMessage.stream()
                                        .filter(message -> message.getCode()
                                                .equals(idToMap.get(event.getMap().getId()).getNameMessageCode()))
                                        .toList(),
                                mapImageCodeToImage.get(
                                        BrawlStarsImageType.BATTLE_MAP.code(idToMap.get(event.getMap().getId()).getCode()))
                        ),
                        seasonedEventIds.contains(event.getId())
                )).toList();
    }

    public List<NotRegisteredBattleEvent> getNotRegisteredEventList() {
        return unregisteredBattleEventRepository.findAll().stream()
                .filter(BaseEntity::isActive)
                .filter(entity -> entity.getEvent().getBrawlStarsId() != null)
                .filter(entity -> entity.getEvent().getBrawlStarsId() != 0)
                .map(battle -> new NotRegisteredBattleEvent(
                        battle.getEvent().getBrawlStarsId(),
                        battle.getEvent().getMode(),
                        battle.getEvent().getMap()
                )).toList();
    }

    @Transactional
    public void registerEvent(NewBattleEvent newBattleEvent) {
        BattleCollectionEntity battle = battleEventRepository.findAllNotRegisteredEventBattle().stream()
                .filter(b -> Objects.equals(
                        b.getEvent().getBrawlStarsId(), newBattleEvent.brawlStarsId()))
                .findAny()
                .orElseThrow(() -> new AdminException(AdminErrorKind.VALIDATION_FAILED,
                        "이벤트가 존재하지 않습니다. brawlStarsId: " + newBattleEvent.brawlStarsId()));
        BattleMapCollectionEntity battleMap = battleMapRepository.findByCode(newBattleEvent.mapCode())
                .orElseThrow(() -> new AdminException(AdminErrorKind.VALIDATION_FAILED,
                        "맵이 존재하지 않습니다. code: " + newBattleEvent.mapCode()));
        BattleEventMode battleEventMode = Arrays.stream(BattleEventMode.values())
                .filter(mode -> mode.getCode().equals(battle.getEvent().getMode()))
                .findAny()
                .orElseThrow(() -> new AdminException(AdminErrorKind.VALIDATION_FAILED,
                        "모드가 존재하지 않습니다. mode: " + battle.getEvent().getMode()));
        battleEventRepository.save(
                new BattleEventCollectionEntity(
                        newBattleEvent.brawlStarsId(),
                        battleEventMode,
                        battleMap
                )
        );

        unregisteredBattleEventRepository.findByEventBrawlStarsId(newBattleEvent.brawlStarsId())
                .ifPresent(unregisteredEvent -> {
                    unregisteredEvent.delete();
                    unregisteredBattleEventRepository.save(unregisteredEvent);
                });
    }

    public void eventSeasoned(long eventId) {
        BattleEventCollectionEntity event = battleEventRepository.findById(eventId)
                .orElseThrow(() -> new AdminException(AdminErrorKind.VALIDATION_FAILED,
                        "이벤트가 존재하지 않습니다. eventId: " + eventId));
        seasonedBattleEventRepository.save(
                seasonedBattleEventRepository.findByBattleEvent(event)
                        .map(seasonedEvent -> {
                            seasonedEvent.restore();
                            return seasonedEvent;
                        })
                        .orElseGet(() -> new SeasonBattleEventCollectionEntity(event))
        );
    }

    @Transactional
    public void eventUnseasoned(long eventId) {
        var event = battleEventRepository.findById(eventId)
                .orElseThrow(() -> new AdminException(AdminErrorKind.VALIDATION_FAILED,
                        "이벤트가 존재하지 않습니다. eventId: " + eventId));
        seasonedBattleEventRepository.findByBattleEvent(event)
                .ifPresent(BaseEntity::delete);
    }

    @Transactional
    public void deleteEvent(long eventId) {
        battleEventRepository.findById(eventId)
                .ifPresent(BaseEntity::delete);
    }

    @Transactional
    public void restoreEvent(long eventId) {
        battleEventRepository.findById(eventId)
                .ifPresent(BaseEntity::restore);
    }
}
