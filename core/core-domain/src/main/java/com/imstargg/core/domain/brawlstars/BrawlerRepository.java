package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.core.enums.BrawlStarsImageType;
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
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BrawlerRepository {

    private final BrawlerJpaRepository brawlerJpaRepository;
    private final BrawlerGearJpaRepository brawlerGearJpaRepository;
    private final GearJpaRepository gearJpaRepository;
    private final StarPowerJpaRepository starPowerJpaRepository;
    private final GadgetJpaRepository gadgetJpaRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;

    public BrawlerRepository(
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

    public Optional<Brawler> find(@Nullable BrawlStarsId id, Language language) {
        if (id == null) {
            return Optional.empty();
        }

        return brawlerJpaRepository.findByBrawlStarsId(id.value()).map(brawlerEntity -> {
                    List<GearEntity> gearEntities = gearJpaRepository.findAllById(
                            brawlerGearJpaRepository.findAllByBrawlerId(brawlerEntity.getId()).stream()
                                    .map(BrawlerGearEntity::getGearId)
                                    .toList()
                    );
                    List<StarPowerEntity> starPowerEntities = starPowerJpaRepository.findAllByBrawlerId(brawlerEntity.getId());
                    List<GadgetEntity> gadgetEntities = gadgetJpaRepository.findAllByBrawlerId(brawlerEntity.getId());
                    Map<String, MessageCollection> codeToMessageCollection = getCodeToMessageCollection(
                            brawlerEntity,
                            gearEntities,
                            starPowerEntities,
                            gadgetEntities
                    );

                    return mapToBrawler(
                            brawlerEntity,
                            gadgetEntities,
                            gearEntities,
                            starPowerEntities,
                            language,
                            codeToMessageCollection
                    );
                }
        );
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
                        mapToStarPower(language, starPowerEntity, codeToMessageCollection)).toList(),
                brawlStarsImageJpaRepository.findByCode(BrawlStarsImageType.BRAWLER_PROFILE.code(
                        String.valueOf(brawlerEntity.getBrawlStarsId())
                )).map(BrawlStarsImageEntity::getUrl).orElse(null)
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
            BrawlerEntity brawlerEntity,
            Collection<GearEntity> gearEntities,
            Collection<StarPowerEntity> starPowerEntities,
            Collection<GadgetEntity> gadgetEntities) {
        List<String> codes = Stream.of(
                List.of(brawlerEntity.getNameMessageCode()),
                gearEntities.stream().map(GearEntity::getNameMessageCode).toList(),
                starPowerEntities.stream().map(StarPowerEntity::getNameMessageCode).toList(),
                gadgetEntities.stream().map(GadgetEntity::getNameMessageCode).toList()
        ).flatMap(List::stream).toList();
        return messageRepository.getCollectionList(codes).stream()
                .collect(Collectors.toMap(MessageCollection::getCode, Function.identity()));
    }
}
