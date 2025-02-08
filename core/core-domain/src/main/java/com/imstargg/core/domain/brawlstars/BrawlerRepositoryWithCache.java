package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.domain.MessageCollection;
import com.imstargg.core.domain.MessageRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
import com.imstargg.storage.db.core.brawlstars.BrawlerEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlerJpaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BrawlerRepositoryWithCache {

    private final BrawlerJpaRepository brawlerJpaRepository;
    private final GearRepositoryWithCache gearRepository;
    private final GadgetRepositoryWithCache gadgetRepository;
    private final StarPowerRepositoryWithCache starPowerRepository;
    private final BrawlStarsImageJpaRepository brawlStarsImageJpaRepository;
    private final MessageRepository messageRepository;

    private final ConcurrentHashMap<BrawlStarsId, BrawlerEntity> brawlerIdToEntityCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, MessageCollection> codeToMessageCollectionCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BrawlStarsImageEntity> codeToImageEntityCache = new ConcurrentHashMap<>();

    public BrawlerRepositoryWithCache(
            BrawlerJpaRepository brawlerJpaRepository,
            GearRepositoryWithCache gearRepository,
            GadgetRepositoryWithCache gadgetRepository,
            StarPowerRepositoryWithCache starPowerRepository,
            BrawlStarsImageJpaRepository brawlStarsImageJpaRepository,
            MessageRepository messageRepository
    ) {
        this.brawlerJpaRepository = brawlerJpaRepository;
        this.gearRepository = gearRepository;
        this.gadgetRepository = gadgetRepository;
        this.starPowerRepository = starPowerRepository;
        this.brawlStarsImageJpaRepository = brawlStarsImageJpaRepository;
        this.messageRepository = messageRepository;
    }

    @PostConstruct
    void init() {
        initBrawler();
        initMessage();
        initImage();
    }

    private void initBrawler() {
        brawlerJpaRepository.findAll().forEach(brawlerEntity ->
                brawlerIdToEntityCache.put(new BrawlStarsId(brawlerEntity.getBrawlStarsId()), brawlerEntity)
        );
    }

    private void initMessage() {
        messageRepository.getCollectionList(
                brawlerIdToEntityCache.values().stream()
                        .map(BrawlerEntity::getNameMessageCode)
                        .toList()
        ).forEach(messageCollection -> codeToMessageCollectionCache.put(messageCollection.code(), messageCollection));
    }

    private void initImage() {
        brawlStarsImageJpaRepository.findAllByCodeIn(
                brawlerIdToEntityCache.values().stream()
                        .map(entity -> BrawlStarsImageType.BRAWLER_PROFILE.code(entity.getBrawlStarsId()))
                        .toList()
        ).forEach(imageEntity ->
                codeToImageEntityCache.put(imageEntity.getCode(), imageEntity)
        );
    }

    public Optional<Brawler> find(BrawlStarsId id) {
        return Optional.ofNullable(brawlerIdToEntityCache.get(id))
                .map(this::mapToBrawler);
    }

    public List<Brawler> findAll() {
        return brawlerIdToEntityCache.values().stream()
                .map(this::mapToBrawler)
                .sorted(Comparator.comparingLong(brawler -> brawler.id().value()))
                .toList();
    }

    private Brawler mapToBrawler(BrawlerEntity brawlerEntity) {
        BrawlStarsId brawlerId = new BrawlStarsId(brawlerEntity.getBrawlStarsId());
        return new Brawler(
                brawlerId,
                codeToMessageCollectionCache.get(brawlerEntity.getNameMessageCode()),
                brawlerEntity.getRarity(),
                brawlerEntity.getRole(),
                gadgetRepository.findAllByBrawlerId(brawlerId),
                gearRepository.findAllByBrawlerId(brawlerId),
                starPowerRepository.findAllByBrawlerId(brawlerId),
                Optional.ofNullable(
                        codeToImageEntityCache.get(
                                BrawlStarsImageType.BRAWLER_PROFILE.code(brawlerEntity.getBrawlStarsId()))
                ).map(BrawlStarsImageEntity::getStoredName).orElse(null)
        );
    }

}
