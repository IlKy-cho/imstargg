package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.core.enums.Language;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
import com.imstargg.storage.db.core.brawlstars.GadgetEntity;
import com.imstargg.storage.db.core.brawlstars.GadgetJpaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.groupingBy;

@Component
public class GadgetRepositoryWithCache {

    private final GadgetJpaRepository gadgetJpaRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;

    private final ConcurrentHashMap<BrawlStarsId, List<GadgetEntity>> brawlerIdToGadgetEntitiesCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, MessageCollection> codeToMessageCollectionCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BrawlStarsImageEntity> codeToImageEntityCache = new ConcurrentHashMap<>();

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
        initGadget();
        initMessage();
        initImage();
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
        messageRepository.getCollectionList(
                brawlerIdToGadgetEntitiesCache.values().stream()
                        .flatMap(List::stream)
                        .map(GadgetEntity::getNameMessageCode)
                        .toList()
        ).forEach(messageCollection -> codeToMessageCollectionCache.put(messageCollection.code(), messageCollection));
    }

    private void initImage() {
        brawlStarsImageJpaRepository.findAllByCodeIn(
                brawlerIdToGadgetEntitiesCache.values().stream()
                        .flatMap(List::stream)
                        .map(GadgetEntity::getBrawlStarsId)
                        .map(BrawlStarsImageType.GADGET::code)
                        .toList()
        ).forEach(imageEntity -> codeToImageEntityCache.put(imageEntity.getCode(), imageEntity));
    }

    public List<Gadget> findAllByBrawlerId(BrawlStarsId brawlerId, Language language) {
        return brawlerIdToGadgetEntitiesCache.getOrDefault(brawlerId, List.of()).stream()
                .map(gadgetEntity -> new Gadget(
                        new BrawlStarsId(gadgetEntity.getBrawlStarsId()),
                        codeToMessageCollectionCache.get(gadgetEntity.getNameMessageCode())
                                .find(language)
                                .orElseThrow(() -> new IllegalStateException(
                                        "가젯 이름을 찾을 수 없습니다. gadgetId=" + gadgetEntity.getBrawlStarsId()))
                                .content(),
                        Optional.ofNullable(
                                codeToImageEntityCache.get(
                                        BrawlStarsImageType.GADGET.code(gadgetEntity.getBrawlStarsId())
                                )
                        ).map(BrawlStarsImageEntity::getStoredName).orElse(null)
                )).toList();
    }
}
