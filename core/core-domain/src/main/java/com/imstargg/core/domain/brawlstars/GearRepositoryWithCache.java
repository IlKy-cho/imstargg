package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.storage.db.core.MessageCodes;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
import com.imstargg.storage.db.core.brawlstars.BrawlerGearEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlerGearJpaRepository;
import com.imstargg.storage.db.core.brawlstars.GearEntity;
import com.imstargg.storage.db.core.brawlstars.GearJpaRepository;
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
public class GearRepositoryWithCache {

    private final BrawlerGearJpaRepository brawlerGearJpaRepository;
    private final GearJpaRepository gearJpaRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;

    private final ConcurrentHashMap<BrawlStarsId, List<BrawlStarsId>> brawlerIdToGearIdsCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BrawlStarsId, Gear> idToGear = new ConcurrentHashMap<>();

    public GearRepositoryWithCache(
            BrawlerGearJpaRepository brawlerGearJpaRepository,
            GearJpaRepository gearJpaRepository,
            BrawlStarsImageJpaRepository brawlStarsImageJpaRepository,
            MessageRepository messageRepository
    ) {
        this.brawlerGearJpaRepository = brawlerGearJpaRepository;
        this.gearJpaRepository = gearJpaRepository;
        this.brawlStarsImageJpaRepository = brawlStarsImageJpaRepository;
        this.messageRepository = messageRepository;
    }

    @PostConstruct
    void init() {
        brawlerGearJpaRepository.findAll().stream()
                .filter(BrawlerGearEntity::isActive)
                .collect(groupingBy(BrawlerGearEntity::getBrawlerBrawlStarsId))
                .forEach((brawlerBrawlStarsId, brawlerGearEntities) -> brawlerIdToGearIdsCache
                        .put(
                                new BrawlStarsId(brawlerBrawlStarsId),
                                brawlerGearEntities.stream()
                                        .map(BrawlerGearEntity::getGearBrawlStarsId)
                                        .map(BrawlStarsId::new)
                                        .toList()
                        ));

        Map<Long, GearEntity> brawlStarsIdToGearEntity = gearJpaRepository.findAll().stream()
                .filter(GearEntity::isActive)
                .collect(toMap(GearEntity::getBrawlStarsId, Function.identity()));
        Map<String, MessageCollection> codeToMessageCollection = messageRepository.getCollectionList(
                brawlStarsIdToGearEntity.values().stream()
                        .map(GearEntity::getBrawlStarsId)
                        .map(MessageCodes.GEAR_NAME::code)
                        .toList()
        ).stream().collect(toMap(MessageCollection::code, Function.identity()));
        Map<String, BrawlStarsImageEntity> codeToImageEntity = brawlStarsImageJpaRepository.findAllByCodeIn(
                brawlStarsIdToGearEntity.keySet().stream()
                        .map(BrawlStarsImageType.GEAR::code)
                        .toList()
        ).stream().collect(toMap(BrawlStarsImageEntity::getCode, Function.identity()));

        brawlStarsIdToGearEntity.forEach((brawlStarsId, gearEntity) -> idToGear.put(
                new BrawlStarsId(brawlStarsId),
                new Gear(
                        new BrawlStarsId(gearEntity.getBrawlStarsId()),
                        gearEntity.getRarity(),
                        codeToMessageCollection.get(gearEntity.getNameMessageCode()),
                        Optional.ofNullable(
                                codeToImageEntity.get(
                                        BrawlStarsImageType.GEAR.code(gearEntity.getBrawlStarsId())
                                )
                        ).map(BrawlStarsImageEntity::getStoredName).orElse(null)
                )
        ));
    }


    public List<Gear> findAllByBrawlerId(BrawlStarsId brawlerId) {
        return brawlerIdToGearIdsCache.getOrDefault(brawlerId, List.of()).stream()
                .map(idToGear::get)
                .toList();
    }
}
