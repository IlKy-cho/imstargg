package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
import com.imstargg.core.enums.Language;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlerEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlerGearEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlerGearJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlerJpaRepository;
import com.imstargg.storage.db.core.brawlstars.GadgetEntity;
import com.imstargg.storage.db.core.brawlstars.GadgetJpaRepository;
import com.imstargg.storage.db.core.brawlstars.GearEntity;
import com.imstargg.storage.db.core.brawlstars.GearJpaRepository;
import com.imstargg.storage.db.core.brawlstars.StarPowerEntity;
import com.imstargg.storage.db.core.brawlstars.StarPowerJpaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Component
public class BrawlerRepositoryWithCache {

    private final BrawlerJpaRepository brawlerJpaRepository;
    private final BrawlerGearJpaRepository brawlerGearJpaRepository;
    private final GearJpaRepository gearJpaRepository;
    private final StarPowerJpaRepository starPowerJpaRepository;
    private final GadgetJpaRepository gadgetJpaRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;

    private final ConcurrentHashMap<BrawlStarsId, BrawlerEntity> brawlerIdToEntityCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BrawlStarsId, List<GearEntity>> brawlerIdToGearEntitiesCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BrawlStarsId, List<StarPowerEntity>> brawlerIdToStarPowerEntitiesCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<BrawlStarsId, List<GadgetEntity>> brawlerIdToGadgetEntitiesCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, MessageCollection> codeToMessageCollectionCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BrawlStarsImageEntity> codeToImageEntityCache = new ConcurrentHashMap<>();

    public BrawlerRepositoryWithCache(
            BrawlerJpaRepository brawlerJpaRepository,
            BrawlerGearJpaRepository brawlerGearJpaRepository,
            GearJpaRepository gearJpaRepository,
            StarPowerJpaRepository starPowerJpaRepository,
            GadgetJpaRepository gadgetJpaRepository,
            BrawlStarsImageJpaRepository brawlStarsImageJpaRepository,
            MessageRepository messageRepository
    ) {
        this.brawlerJpaRepository = brawlerJpaRepository;
        this.brawlerGearJpaRepository = brawlerGearJpaRepository;
        this.gearJpaRepository = gearJpaRepository;
        this.starPowerJpaRepository = starPowerJpaRepository;
        this.gadgetJpaRepository = gadgetJpaRepository;
        this.brawlStarsImageJpaRepository = brawlStarsImageJpaRepository;
        this.messageRepository = messageRepository;
    }

    @PostConstruct
    void init() {
        initBrawler();
        initGear();
        initStarPower();
        initGadget();
        initMessage();
        initImage();
    }

    private void initBrawler() {
        brawlerJpaRepository.findAll().forEach(brawlerEntity ->
                brawlerIdToEntityCache.put(new BrawlStarsId(brawlerEntity.getBrawlStarsId()), brawlerEntity)
        );
    }

    private void initGear() {
        Map<Long, GearEntity> gearBrawlStarsIdToEntity = gearJpaRepository.findAll().stream()
                .collect(toMap(GearEntity::getBrawlStarsId, Function.identity()));
        brawlerGearJpaRepository.findAll()
                .stream()
                .collect(groupingBy(BrawlerGearEntity::getBrawlerBrawlStarsId))
                .forEach((brawlerId, brawlerGearEntities) ->
                        brawlerIdToGearEntitiesCache.put(
                                new BrawlStarsId(brawlerId),
                                brawlerGearEntities.stream()
                                        .map(brawlerGearEntity -> gearBrawlStarsIdToEntity.get(brawlerGearEntity.getGearBrawlStarsId()))
                                        .toList()
                        )
                );
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

    private void initGadget() {
        gadgetJpaRepository.findAll().stream()
                .collect(groupingBy(GadgetEntity::getBrawlerBrawlStarsId))
                .forEach((brawlerId, gadgetEntities) ->
                        brawlerIdToGadgetEntitiesCache.put(
                                new BrawlStarsId(brawlerId),
                                gadgetEntities
                        )
                );
    }

    private void initMessage() {
        List<String> codes = Stream.of(
                brawlerIdToEntityCache.values().stream()
                        .map(BrawlerEntity::getNameMessageCode).toList(),
                brawlerIdToGearEntitiesCache.values().stream().flatMap(Collection::stream)
                        .map(GearEntity::getNameMessageCode).toList(),
                brawlerIdToStarPowerEntitiesCache.values().stream().flatMap(Collection::stream)
                        .map(StarPowerEntity::getNameMessageCode).toList(),
                brawlerIdToGadgetEntitiesCache.values().stream().flatMap(Collection::stream)
                        .map(GadgetEntity::getNameMessageCode).toList()
        ).flatMap(List::stream).toList();

        messageRepository.getCollectionList(codes).forEach(messageCollection ->
                codeToMessageCollectionCache.put(messageCollection.getCode(), messageCollection)
        );
    }

    private void initImage() {
        List<String> codes = brawlerIdToEntityCache.values().stream()
                .map(entity -> BrawlStarsImageType.BRAWLER_PROFILE.code(entity.getBrawlStarsId()))
                .toList();
        brawlStarsImageJpaRepository.findAllByCodeIn(codes).forEach(imageEntity ->
                codeToImageEntityCache.put(imageEntity.getCode(), imageEntity)
        );
    }

    public Optional<Brawler> find(BrawlStarsId id, Language language) {
        return Optional.ofNullable(brawlerIdToEntityCache.get(id))
                .map(brawlerEntity -> mapToBrawler(brawlerEntity, language));
    }

    public List<Brawler> findAll(Language language) {
        return brawlerIdToEntityCache.values().stream()
                .map(brawlerEntity -> mapToBrawler(brawlerEntity, language))
                .toList();
    }

    private Brawler mapToBrawler(
            BrawlerEntity brawlerEntity,
            Language language
    ) {
        BrawlStarsId brawlerId = new BrawlStarsId(brawlerEntity.getBrawlStarsId());
        return new Brawler(
                brawlerId,
                codeToMessageCollectionCache.get(brawlerEntity.getNameMessageCode())
                        .find(language)
                        .orElseThrow(() -> new IllegalStateException(
                                "브롤러 이름을 찾을 수 없습니다. brawlerId=" + brawlerEntity.getId()))
                        .content(),
                brawlerEntity.getRarity(),
                brawlerEntity.getRole(),
                brawlerIdToGadgetEntitiesCache.getOrDefault(brawlerId, List.of()).stream().map(gadgetEntity ->
                        mapToGadget(gadgetEntity, language)).toList(),
                brawlerIdToGearEntitiesCache.getOrDefault(brawlerId, List.of()).stream().map(gearEntity ->
                        mapToGear(gearEntity, language)).toList(),
                brawlerIdToStarPowerEntitiesCache.getOrDefault(brawlerId, List.of()).stream().map(starPowerEntity ->
                        mapToStarPower(starPowerEntity, language)).toList(),
                Optional.ofNullable(
                        codeToImageEntityCache.get(
                                BrawlStarsImageType.BRAWLER_PROFILE.code(brawlerEntity.getBrawlStarsId()))
                ).map(BrawlStarsImageEntity::getStoredName).orElse(null)
        );
    }

    private Gadget mapToGadget(
            GadgetEntity gadgetEntity,
            Language language
    ) {
        return new Gadget(
                new BrawlStarsId(gadgetEntity.getBrawlStarsId()),
                codeToMessageCollectionCache.get(gadgetEntity.getNameMessageCode())
                        .find(language)
                        .orElseThrow(() -> new IllegalStateException(
                                "가젯 이름을 찾을 수 없습니다. gadgetId=" + gadgetEntity.getId()))
                        .content()
        );
    }

    private Gear mapToGear(
            GearEntity gearEntity,
            Language language
    ) {
        return new Gear(
                new BrawlStarsId(gearEntity.getBrawlStarsId()),
                gearEntity.getRarity(),
                codeToMessageCollectionCache.get(gearEntity.getNameMessageCode())
                        .find(language)
                        .orElseThrow(() -> new IllegalStateException(
                                "기어 이름을 찾을 수 없습니다. gearId=" + gearEntity.getId()))
                        .content()
        );
    }

    private StarPower mapToStarPower(
            StarPowerEntity starPowerEntity,
            Language language
    ) {
        return new StarPower(
                new BrawlStarsId(starPowerEntity.getBrawlStarsId()),
                codeToMessageCollectionCache.get(starPowerEntity.getNameMessageCode())
                        .find(language)
                        .orElseThrow(() -> new IllegalStateException(
                                "스타파워 이름을 찾을 수 없습니다. starPowerId=" + starPowerEntity.getId()))
                        .content()
        );
    }
}
