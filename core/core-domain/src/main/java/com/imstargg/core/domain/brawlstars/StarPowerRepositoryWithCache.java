package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.storage.db.core.MessageCodes;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
import com.imstargg.storage.db.core.brawlstars.StarPowerEntity;
import com.imstargg.storage.db.core.brawlstars.StarPowerJpaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Component
public class StarPowerRepositoryWithCache {

    private final StarPowerJpaRepository starPowerJpaRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;

    private final ConcurrentHashMap<BrawlStarsId, List<StarPower>> brawlerIdToStarPowersCache = new ConcurrentHashMap<>();


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
        Map<Long, List<StarPowerEntity>> brawlerBrawlStarsIdToStarPowerEntities = starPowerJpaRepository.findAll()
                .stream()
                .filter(StarPowerEntity::isActive)
                .collect(groupingBy(StarPowerEntity::getBrawlerBrawlStarsId));
        Map<String, MessageCollection> codeToMessageCollection = messageRepository.getCollectionList(
                brawlerBrawlStarsIdToStarPowerEntities.values().stream()
                        .flatMap(List::stream)
                        .map(StarPowerEntity::getBrawlStarsId)
                        .map(MessageCodes.STAR_POWER_NAME::code)
                        .toList()
        ).stream().collect(toMap(MessageCollection::code, Function.identity()));
        Map<String, BrawlStarsImageEntity> codeToImageEntity = brawlStarsImageJpaRepository.findAllByCodeIn(
                brawlerBrawlStarsIdToStarPowerEntities.values().stream()
                        .flatMap(List::stream)
                        .map(StarPowerEntity::getBrawlStarsId)
                        .map(BrawlStarsImageType.STAR_POWER::code)
                        .toList()
        ).stream().collect(toMap(BrawlStarsImageEntity::getCode, Function.identity()));

        brawlerBrawlStarsIdToStarPowerEntities.forEach((brawlerBrawlStarsId, starPowerEntities) -> {
            brawlerIdToStarPowersCache.put(new BrawlStarsId(brawlerBrawlStarsId), starPowerEntities.stream()
                    .map(starPowerEntity -> new StarPower(
                            new BrawlStarsId(starPowerEntity.getBrawlStarsId()),
                            codeToMessageCollection.get(starPowerEntity.getNameMessageCode()),
                            Optional.ofNullable(
                                    codeToImageEntity.get(
                                            BrawlStarsImageType.STAR_POWER.code(starPowerEntity.getBrawlStarsId())
                                    )
                            ).map(BrawlStarsImageEntity::getStoredName).orElse(null)
                    )).toList());
        });
    }


    public List<StarPower> findAllByBrawlerId(BrawlStarsId brawlerId) {
        return brawlerIdToStarPowersCache.getOrDefault(brawlerId, List.of());
    }
}
