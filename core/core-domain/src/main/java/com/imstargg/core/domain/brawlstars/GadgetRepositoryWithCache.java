package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
import com.imstargg.storage.db.core.brawlstars.GadgetEntity;
import com.imstargg.storage.db.core.brawlstars.GadgetJpaRepository;
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
public class GadgetRepositoryWithCache {

    private final GadgetJpaRepository gadgetJpaRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;

    private final ConcurrentHashMap<BrawlStarsId, List<Gadget>> brawlerIdToGadgetsCache = new ConcurrentHashMap<>();

    public GadgetRepositoryWithCache(
            GadgetJpaRepository gadgetJpaRepository,
            BrawlStarsImageJpaRepository brawlStarsImageJpaRepository,
            MessageRepository messageRepository
    ) {
        this.gadgetJpaRepository = gadgetJpaRepository;
        this.brawlStarsImageJpaRepository = brawlStarsImageJpaRepository;
        this.messageRepository = messageRepository;
    }

    @PostConstruct
    void init() {
        Map<Long, List<GadgetEntity>> brawlerBrawlStarsIdToGadgetEntities = gadgetJpaRepository.findAll().stream()
                .collect(groupingBy(GadgetEntity::getBrawlerBrawlStarsId));
        Map<String, MessageCollection> codeToMessageCollection = messageRepository.getCollectionList(
                brawlerBrawlStarsIdToGadgetEntities.values().stream()
                        .flatMap(List::stream)
                        .map(GadgetEntity::getNameMessageCode)
                        .toList()
        ).stream().collect(toMap(MessageCollection::code, Function.identity()));
        Map<String, BrawlStarsImageEntity> codeToImageEntity = brawlStarsImageJpaRepository.findAllByCodeIn(
                brawlerBrawlStarsIdToGadgetEntities.values().stream()
                        .flatMap(List::stream)
                        .map(GadgetEntity::getBrawlStarsId)
                        .map(BrawlStarsImageType.GADGET::code)
                        .toList()
        ).stream().collect(toMap(BrawlStarsImageEntity::getCode, Function.identity()));

        brawlerBrawlStarsIdToGadgetEntities.forEach((brawlerBrawlStarsId, gadgetEntities) -> {
            brawlerIdToGadgetsCache.put(new BrawlStarsId(brawlerBrawlStarsId), gadgetEntities.stream()
                    .map(gadgetEntity -> new Gadget(
                            new BrawlStarsId(gadgetEntity.getBrawlStarsId()),
                            codeToMessageCollection.get(gadgetEntity.getNameMessageCode()),
                            Optional.ofNullable(
                                    codeToImageEntity.get(
                                            BrawlStarsImageType.GADGET.code(gadgetEntity.getBrawlStarsId())
                                    )
                            ).map(BrawlStarsImageEntity::getStoredName).orElse(null)
                    )).toList());
        });
    }

    public List<Gadget> findAllByBrawlerId(BrawlStarsId brawlerId) {
        return brawlerIdToGadgetsCache.getOrDefault(brawlerId, List.of());
    }
}
