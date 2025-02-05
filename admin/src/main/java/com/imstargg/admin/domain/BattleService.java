package com.imstargg.admin.domain;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.imstargg.core.enums.BattleType;
import com.imstargg.storage.db.core.BattleEntity;
import com.imstargg.storage.db.core.BattleEntityEvent;
import com.imstargg.storage.db.core.BattleJpaRepository;
import com.imstargg.storage.db.core.MessageCodes;
import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

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
    private final BrawlStarsImageCollectionJpaRepository brawlStarsImageRepository;
    private final MessageCollectionJpaRepository messageRepository;

    public BattleService(
            BrawlStarsImageUploader brawlStarsImageUploader,
            BattleJpaRepository battleMapRepository,
            BrawlStarsImageCollectionJpaRepository brawlStarsImageRepository,
            MessageCollectionJpaRepository messageRepository
    ) {
        this.brawlStarsImageUploader = brawlStarsImageUploader;
        this.battleJpaRepository = battleMapRepository;
        this.brawlStarsImageRepository = brawlStarsImageRepository;
        this.messageRepository = messageRepository;
    }


    public List<BattleEvent> getEventList() {
        List<BattleEntity> battles = battleJpaRepository.findAllDistinctEventBrawlStarsIdsByBattleTypeInAndGreaterThanEqualBattleTime(
                        null,
                        null
                )
                .stream()
                .map(eventBrawlStarsId ->
                        eventBrawlStarsIdToLastBattleCache.get(eventBrawlStarsId, key ->
                                battleJpaRepository
                                        .findLatestBattleByEventBrawlStarsIdAndBattleTypeIn(eventBrawlStarsId, BattleType.regularTypes())
                        )
                )
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        Map<String, List<MessageCollectionEntity>> mapCodeToNames = messageRepository.findAllByCodeIn(
                        battles.stream()
                                .map(BattleEntity::getEvent)
                                .map(BattleEntityEvent::getMap)
                                .filter(Objects::nonNull)
                                .map(MessageCodes.BATTLE_MAP_NAME::code)
                                .toList()
                ).stream()
                .collect(Collectors.groupingBy(MessageCollectionEntity::getCode));
        Map<String, BrawlStarsImageCollectionEntity> mapImageCodeToImage = brawlStarsImageRepository
                .findAllByType(BrawlStarsImageType.BATTLE_MAP)
                .stream()
                .collect(toMap(BrawlStarsImageCollectionEntity::getCode, Function.identity()));

        return battles.stream()
                .map(battle -> new BattleEvent(
                        battle.getEvent(),
                        new BattleEventMap(
                                Optional.ofNullable(battle.getEvent().getMap())
                                        .map(MessageCodes.BATTLE_MAP_NAME::code)
                                        .map(mapCodeToNames::get)
                                        .orElse(List.of()),
                                battle.getEvent().getBrawlStarsId() == null ? null :
                                        mapImageCodeToImage.get(BrawlStarsImageType.BATTLE_MAP.code(battle.getEvent().getBrawlStarsId()))
                        ),
                        battle.getBattleTime().toLocalDateTime()
                ))
                .toList();
    }

    public void uploadMapImage(long eventBrawlStarsId, Resource resource) {
        brawlStarsImageUploader.uploadMap(eventBrawlStarsId, resource);
    }
}
