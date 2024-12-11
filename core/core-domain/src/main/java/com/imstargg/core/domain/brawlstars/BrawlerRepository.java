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
import jakarta.annotation.Nullable;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@CacheConfig(cacheNames = "brawler")
public class BrawlerRepository {

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
    }

    @Cacheable(key = "'brawlers:v1:' + #language.name() + ':' + #id.value()")
    public Optional<Brawler> find(@Nullable BrawlStarsId id, Language language) {
        if (id == null) {
            return Optional.empty();
        }

        Optional<BrawlerEntity> brawlerEntityOpt = brawlerJpaRepository.findByBrawlStarsId(id.value());
        if (brawlerEntityOpt.isEmpty()) {
            return Optional.empty();
        }

        BrawlerEntity brawlerEntity = brawlerEntityOpt.get();
        List<GearEntity> gearEntities = gearJpaRepository.findAllById(
                brawlerGearJpaRepository.findAllByBrawlerId(brawlerEntity.getId()).stream()
                        .map(BrawlerGearEntity::getGearId)
                        .toList()
        );
        List<StarPowerEntity> starPowerEntities = starPowerJpaRepository.findAllByBrawlerId(brawlerEntity.getId());
        List<GadgetEntity> gadgetEntities = gadgetJpaRepository.findAllByBrawlerId(brawlerEntity.getId());
        Map<String, MessageCollection> codeToMessageCollection = getCodeToMessageCollection(
                List.of(brawlerEntity),
                gearEntities,
                starPowerEntities,
                gadgetEntities
        );

        return Optional.of(
                mapToBrawler(
                        brawlerEntity,
                        gadgetEntities,
                        gearEntities,
                        starPowerEntities,
                        language,
                        codeToMessageCollection
                )
        );
    }

    @Cacheable(key = "'brawlers:v1:' + #language.name()")
    public List<Brawler> findAll(Language language) {
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

        return brawlerEntities.stream()
                .map(brawlerEntity -> mapToBrawler(
                        brawlerEntity,
                        brawlerIdToGadgetEntities.get(brawlerEntity.getId()),
                        brawlerIdToBrawlerGearEntities.get(brawlerEntity.getId()).stream()
                                .map(brawlerGear -> idToGearEntities.get(brawlerGear.getGearId()))
                                .toList(),
                        brawlerIdToStarPowerEntities.get(brawlerEntity.getId()),
                        language,
                        codeToMessageCollection
                )).toList();
    }

    private Brawler mapToBrawler(
            BrawlerEntity brawlerEntity,
            List<GadgetEntity> gadgetEntities,
            List<GearEntity> gearEntities,
            List<StarPowerEntity> starPowerEntities,
            Language language,
            Map<String, MessageCollection> codeToMessageCollection
    ) {
        return new Brawler(
                new BrawlStarsId(brawlerEntity.getBrawlStarsId()),
                codeToMessageCollection.get(brawlerEntity.getNameMessageCode()).find(language)
                        .orElseThrow(() -> new IllegalStateException(
                                "브롤러 이름을 찾을 수 없습니다. brawlerId=" + brawlerEntity.getId()))
                        .content(),
                brawlerEntity.getRarity(),
                gadgetEntities.stream().map(gadgetEntity ->
                        mapToGadget(language, gadgetEntity, codeToMessageCollection)).toList(),
                gearEntities.stream().map(gearEntity ->
                        mapToGear(language, gearEntity, codeToMessageCollection)).toList(),
                starPowerEntities.stream().map(starPowerEntity ->
                        mapToStarPower(language, starPowerEntity, codeToMessageCollection)).toList()
        );
    }

    private StarPower mapToStarPower(
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

    private Gear mapToGear(
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

    private Gadget mapToGadget(
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
