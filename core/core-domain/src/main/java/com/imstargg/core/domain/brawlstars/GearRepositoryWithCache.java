package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.MessageRepository;
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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class GearRepositoryWithCache {

    private final BrawlerGearJpaRepository brawlerGearJpaRepository;
    private final GearJpaRepository gearJpaRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;

    private final ConcurrentHashMap<BrawlStarsId, List<BrawlStarsId>> brawlerIdToGearIdsCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BrawlStarsId, GearEntity> gearIdToEntityCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, MessageCollection> codeToMessageCollectionCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BrawlStarsImageEntity> codeToImageEntityCache = new ConcurrentHashMap<>();


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
        initGear();
        initMessage();
        initImage();
    }

    private void initGear() {
        brawlerGearJpaRepository.findAll().stream()
                .collect(Collectors.groupingBy(BrawlerGearEntity::getBrawlerBrawlStarsId))
                .forEach((brawlerBrawlStarsId, brawlerGearEntities) -> brawlerIdToGearIdsCache
                        .put(new BrawlStarsId(brawlerBrawlStarsId), brawlerGearEntities.stream()
                                .map(BrawlerGearEntity::getGearBrawlStarsId)
                                .map(BrawlStarsId::new)
                                .toList()));
        gearJpaRepository.findAll().forEach(gearEntity -> gearIdToEntityCache
                .put(new BrawlStarsId(gearEntity.getBrawlStarsId()), gearEntity));
    }

    private void initMessage() {
        messageRepository.getCollectionList(
                gearIdToEntityCache.values().stream()
                        .map(GearEntity::getNameMessageCode)
                        .toList()
        ).forEach(messageCollection -> codeToMessageCollectionCache.put(messageCollection.code(), messageCollection));
    }

    private void initImage() {
        brawlStarsImageJpaRepository.findAllByCodeIn(
                gearIdToEntityCache.keySet().stream()
                        .map(BrawlStarsId::value)
                        .map(BrawlStarsImageType.GEAR::code)
                        .toList()
        ).forEach(imageEntity -> codeToImageEntityCache.put(imageEntity.getCode(), imageEntity));
    }

    public List<Gear> findAllByBrawlerId(BrawlStarsId brawlerId) {
        return brawlerIdToGearIdsCache.getOrDefault(brawlerId, List.of()).stream()
                .map(gearIdToEntityCache::get)
                .map(gearEntity -> new Gear(
                        new BrawlStarsId(gearEntity.getBrawlStarsId()),
                        gearEntity.getRarity(),
                        codeToMessageCollectionCache.get(gearEntity.getNameMessageCode()),
                        Optional.ofNullable(
                                codeToImageEntityCache.get(
                                        BrawlStarsImageType.GEAR.code(gearEntity.getBrawlStarsId())
                                )
                        ).map(BrawlStarsImageEntity::getStoredName).orElse(null)
                )).toList();
    }
}
