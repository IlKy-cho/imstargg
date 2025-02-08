package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
import com.imstargg.storage.db.core.brawlstars.StarPowerEntity;
import com.imstargg.storage.db.core.brawlstars.StarPowerJpaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.groupingBy;

@Component
public class StarPowerRepositoryWithCache {

    private final StarPowerJpaRepository starPowerJpaRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;

    private final ConcurrentHashMap<BrawlStarsId, List<StarPowerEntity>> brawlerIdToStarPowerEntitiesCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, MessageCollection> codeToMessageCollectionCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BrawlStarsImageEntity> codeToImageEntityCache = new ConcurrentHashMap<>();

    public StarPowerRepositoryWithCache(
            StarPowerJpaRepository starPowerJpaRepository,
            BrawlStarsImageJpaRepository brawlStarsImageJpaRepository,
            MessageRepository messageRepository
    ) {
        this.starPowerJpaRepository = starPowerJpaRepository;
        this.brawlStarsImageJpaRepository = brawlStarsImageJpaRepository;
        this.messageRepository = messageRepository;
    }

    @PostConstruct
    void init() {
        initStarPower();
        initMessage();
        initImage();
    }

    private void initStarPower() {
        starPowerJpaRepository.findAll().stream()
                .collect(groupingBy(StarPowerEntity::getBrawlerBrawlStarsId))
                .forEach((brawlerId, starPowerEntities) ->
                        brawlerIdToStarPowerEntitiesCache.put(
                                new BrawlStarsId(brawlerId),
                                starPowerEntities
                        )
                );
    }

    private void initMessage() {
        messageRepository.getCollectionList(
                brawlerIdToStarPowerEntitiesCache.values().stream()
                        .flatMap(List::stream)
                        .map(StarPowerEntity::getNameMessageCode)
                        .toList()
        ).forEach(messageCollection -> codeToMessageCollectionCache.put(messageCollection.code(), messageCollection));
    }

    private void initImage() {
        brawlStarsImageJpaRepository.findAllByCodeIn(
                brawlerIdToStarPowerEntitiesCache.values().stream()
                        .flatMap(List::stream)
                        .map(StarPowerEntity::getBrawlStarsId)
                        .map(BrawlStarsImageType.STAR_POWER::code)
                        .toList()
        ).forEach(imageEntity -> codeToImageEntityCache.put(imageEntity.getCode(), imageEntity));
    }

    public List<StarPower> findAllByBrawlerId(BrawlStarsId brawlerId) {
        return brawlerIdToStarPowerEntitiesCache.getOrDefault(brawlerId, List.of()).stream()
                .map(starPowerEntity -> new StarPower(
                        new BrawlStarsId(starPowerEntity.getBrawlStarsId()),
                        codeToMessageCollectionCache.get(starPowerEntity.getNameMessageCode()),
                        Optional.ofNullable(
                                codeToImageEntityCache.get(
                                        BrawlStarsImageType.STAR_POWER.code(starPowerEntity.getBrawlStarsId())
                                )
                        ).map(BrawlStarsImageEntity::getStoredName).orElse(null)
                )).toList();
    }
}
