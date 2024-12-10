package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.core.enums.Language;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BrawlerRepository {

    private final Map<Language, Map<BrawlStarsId, Brawler>> cache;

    private final BrawlerJpaRepository brawlerJpaRepository;
    private final BrawlerGearJpaRepository brawlerGearJpaRepository;
    private final GearJpaRepository gearJpaRepository;
    private final StarPowerJpaRepository starPowerJpaRepository;
    private final GadgetJpaRepository gadgetJpaRepository;
    private final MessageRepository messageRepository;

    public BrawlerRepository(
            BrawlerJpaRepository brawlerJpaRepository,
            BrawlerGearJpaRepository brawlerGearJpaRepository,
            GearJpaRepository gearJpaRepository,
            StarPowerJpaRepository starPowerJpaRepository,
            GadgetJpaRepository gadgetJpaRepository,
            MessageRepository messageRepository
    ) {
        this.brawlerJpaRepository = brawlerJpaRepository;
        this.brawlerGearJpaRepository = brawlerGearJpaRepository;
        this.gearJpaRepository = gearJpaRepository;
        this.starPowerJpaRepository = starPowerJpaRepository;
        this.gadgetJpaRepository = gadgetJpaRepository;
        this.messageRepository = messageRepository;

        this.cache = new ConcurrentHashMap<>();
        Arrays.stream(Language.values())
                .forEach(language -> cache.put(language, new ConcurrentHashMap<>()));
    }

    @Scheduled(fixedRate = 120, timeUnit = TimeUnit.MINUTES)
    void init() {
        List<BrawlerEntity> brawlerEntities = brawlerJpaRepository.findAll();
        Map<Long, List<BrawlerGearEntity>> brawlerIdToBrawlerGearEntities = brawlerGearJpaRepository.findAll().stream()
                .collect(Collectors.groupingBy(BrawlerGearEntity::getBrawlerId));
        Map<Long, GearEntity> idToGearEntities = gearJpaRepository.findAll().stream()
                .collect(Collectors.toMap(GearEntity::getId, Function.identity()));
        Map<Long, List<StarPowerEntity>> brawlerIdToStarPowerEntities = starPowerJpaRepository.findAll().stream()
                .collect(Collectors.groupingBy(StarPowerEntity::getBrawlerId));
        Map<Long, List<GadgetEntity>> brawlerIdToGadgetEntities = gadgetJpaRepository.findAll().stream()
                .collect(Collectors.groupingBy(GadgetEntity::getBrawlerId));
        Map<String, MessageCollection> codeToMessageCollection = getCodeToMessageCollection(
                brawlerEntities,
                idToGearEntities.values(),
                brawlerIdToStarPowerEntities.values().stream().flatMap(List::stream).toList(),
                brawlerIdToGadgetEntities.values().stream().flatMap(List::stream).toList()
        );

        for (BrawlerEntity brawlerEntity : brawlerEntities) {
            List<GearEntity> gearEntities = brawlerIdToBrawlerGearEntities.get(brawlerEntity.getId()).stream()
                    .map(brawlerGear -> idToGearEntities.get(brawlerGear.getGearId()))
                    .toList();
            List<StarPowerEntity> starPowerEntities = brawlerIdToStarPowerEntities.get(brawlerEntity.getId());
            List<GadgetEntity> gadgetEntities = brawlerIdToGadgetEntities.get(brawlerEntity.getId());

            BrawlStarsId brawlerId = new BrawlStarsId(brawlerEntity.getBrawlStarsId());
            for (Language language : Language.values()) {
                cache.get(language).put(
                        brawlerId,
                        new Brawler(
                                brawlerId,
                                codeToMessageCollection.get(brawlerEntity.getNameMessageCode()).find(language)
                                        .orElseThrow(() -> new IllegalStateException(
                                                "브롤러 이름을 찾을 수 없습니다. brawlerId=" + brawlerId))
                                        .content(),
                                brawlerEntity.getRarity(),
                                gadgetEntities.stream().map(gadgetEntity ->
                                        mapToGadget(language, gadgetEntity, codeToMessageCollection)).toList(),
                                gearEntities.stream().map(gearEntity ->
                                        mapToGear(language, gearEntity, codeToMessageCollection)).toList(),
                                starPowerEntities.stream().map(starPowerEntity ->
                                        mapToStarPower(language, starPowerEntity, codeToMessageCollection)).toList()
                        ));
            }
        }
    }

    private static StarPower mapToStarPower(
            Language language,
            StarPowerEntity starPowerEntity,
            Map<String, MessageCollection> codeToMessageCollection) {
        return new StarPower(
                new BrawlStarsId(starPowerEntity.getBrawlStarsId()),
                codeToMessageCollection.get(starPowerEntity.getNameMessageCode())
                        .find(language)
                        .orElseThrow(() -> new IllegalStateException(
                                "스타파워 이름을 찾을 수 없습니다. starPowerId=" + starPowerEntity.getId()))
                        .content()
        );
    }

    private static Gear mapToGear(
            Language language,
            GearEntity gearEntity,
            Map<String, MessageCollection> codeToMessageCollection) {
        return new Gear(
                new BrawlStarsId(gearEntity.getBrawlStarsId()),
                gearEntity.getRarity(),
                codeToMessageCollection.get(gearEntity.getNameMessageCode())
                        .find(language)
                        .orElseThrow(() -> new IllegalStateException(
                                "기어 이름을 찾을 수 없습니다. gearId=" + gearEntity.getId()))
                        .content()
        );
    }

    private static Gadget mapToGadget(
            Language language,
            GadgetEntity gadgetEntity,
            Map<String, MessageCollection> codeToMessageCollection) {
        return new Gadget(
                new BrawlStarsId(gadgetEntity.getBrawlStarsId()),
                codeToMessageCollection.get(gadgetEntity.getNameMessageCode())
                        .find(language)
                        .orElseThrow(() -> new IllegalStateException(
                                "가젯 이름을 찾을 수 없습니다. gadgetId=" + gadgetEntity.getId()))
                        .content()
        );
    }

    private Map<String, MessageCollection> getCodeToMessageCollection(
            Collection<BrawlerEntity> brawlerEntities,
            Collection<GearEntity> gearEntities,
            Collection<StarPowerEntity> starPowerEntities,
            Collection<GadgetEntity> gadgetEntities) {
        List<String> codes = Stream.of(
                brawlerEntities.stream().map(BrawlerEntity::getNameMessageCode).toList(),
                gearEntities.stream().map(GearEntity::getNameMessageCode).toList(),
                starPowerEntities.stream().map(StarPowerEntity::getNameMessageCode).toList(),
                gadgetEntities.stream().map(GadgetEntity::getNameMessageCode).toList()
        ).flatMap(List::stream).toList();
        return messageRepository.getCollectionList(codes).stream()
                .collect(Collectors.toMap(MessageCollection::getCode, Function.identity()));
    }
}
