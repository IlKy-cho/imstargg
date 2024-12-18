package com.imstargg.admin.domain;

import com.imstargg.admin.support.error.AdminErrorKind;
import com.imstargg.admin.support.error.AdminException;
import com.imstargg.core.enums.BrawlStarsImageType;
import com.imstargg.core.enums.Language;
import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlerCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlerCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlerGearCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlerGearCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.GadgetCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.GadgetCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.GearCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.GearCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.StarPowerCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.StarPowerCollectionJpaRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class BrawlerService {

    private final BrawlerCollectionJpaRepository brawlerRepository;
    private final GadgetCollectionJpaRepository gadgetRepository;
    private final StarPowerCollectionJpaRepository starPowerRepository;
    private final GearCollectionJpaRepository gearRepository;
    private final BrawlerGearCollectionJpaRepository brawlerGearRepository;
    private final BrawlStarsImageCollectionJpaRepository brawlStarsImageRepository;
    private final MessageCollectionJpaRepository messageRepository;
    private final BrawlStarsImageUploader brawlStarsImageUploader;

    public BrawlerService(
            BrawlerCollectionJpaRepository brawlerRepository,
            GadgetCollectionJpaRepository gadgetRepository,
            StarPowerCollectionJpaRepository starPowerRepository,
            GearCollectionJpaRepository gearRepository,
            BrawlerGearCollectionJpaRepository brawlerGearRepository,
            BrawlStarsImageCollectionJpaRepository brawlStarsImageRepository,
            MessageCollectionJpaRepository messageRepository,
            BrawlStarsImageUploader brawlStarsImageUploader
    ) {
        this.brawlerRepository = brawlerRepository;
        this.gadgetRepository = gadgetRepository;
        this.starPowerRepository = starPowerRepository;
        this.gearRepository = gearRepository;
        this.brawlerGearRepository = brawlerGearRepository;
        this.brawlStarsImageRepository = brawlStarsImageRepository;
        this.messageRepository = messageRepository;
        this.brawlStarsImageUploader = brawlStarsImageUploader;
    }

    public List<Brawler> getList() {
        List<BrawlerCollectionEntity> brawlers = brawlerRepository.findAll();
        Map<Long, List<GadgetCollectionEntity>> brawlerIdToGadgets = gadgetRepository.findAll().stream()
                .collect(groupingBy(GadgetCollectionEntity::getBrawlerId));
        Map<Long, List<StarPowerCollectionEntity>> brawlerIdToStarPowers = starPowerRepository.findAll().stream()
                .collect(groupingBy(StarPowerCollectionEntity::getBrawlerId));
        Map<Long, GearCollectionEntity> idToGear = gearRepository.findAll().stream()
                .collect(toMap(GearCollectionEntity::getId, Function.identity()));
        Map<Long, List<GearCollectionEntity>> brawlerIdToGears = brawlerGearRepository.findAll().stream()
                .collect(groupingBy(BrawlerGearCollectionEntity::getBrawlerId,
                        mapping(
                                brawlerGear -> idToGear.get(brawlerGear.getGearId()),
                                toList()
                        )
                ));
        Map<String, BrawlStarsImageCollectionEntity> codeToBrawlerProfileImage = brawlStarsImageRepository.findAllByType(BrawlStarsImageType.BRAWLER_PROFILE)
                .stream()
                .collect(toMap(BrawlStarsImageCollectionEntity::getCode, Function.identity()));

        Map<String, List<MessageCollectionEntity>> codeToMessages = messageRepository.findAll()
                .stream()
                .collect(groupingBy(MessageCollectionEntity::getCode));

        return brawlers.stream().map(brawler ->
                new Brawler(
                        brawler,
                        codeToMessages.get(brawler.getNameMessageCode()),
                        codeToBrawlerProfileImage.getOrDefault(
                                BrawlStarsImageType.BRAWLER_PROFILE.code(String.valueOf(brawler.getBrawlStarsId())), null
                        ),
                        brawlerIdToGadgets.getOrDefault(brawler.getId(), List.of()).stream()
                                .map(gadget ->
                                        new Gadget(gadget, codeToMessages.get(gadget.getNameMessageCode()))
                                ).toList(),
                        brawlerIdToStarPowers.getOrDefault(brawler.getId(), List.of()).stream()
                                .map(starPower ->
                                        new StarPower(starPower, codeToMessages.get(starPower.getNameMessageCode()))
                                ).toList(),
                        brawlerIdToGears.getOrDefault(brawler.getId(), List.of()).stream()
                                .map(gear ->
                                        new Gear(gear, codeToMessages.get(gear.getNameMessageCode()))
                                ).toList()
                )
        ).toList();
    }

    @Transactional
    public void register(NewBrawler newBrawler) {
        validateNewBrawler(newBrawler);

        newBrawler.names().validate();
        BrawlerCollectionEntity brawler = brawlerRepository.save(new BrawlerCollectionEntity(
                newBrawler.brawlStarsId(),
                newBrawler.rarity(),
                newBrawler.role()
        ));

        newBrawler.names().messages().forEach((language, name) -> messageRepository.save(
                new MessageCollectionEntity(brawler.getNameMessageCode(), language, name)));

        newBrawler.gadgets().forEach(newGadget -> {
            newGadget.names().validate();
            GadgetCollectionEntity gadget = gadgetRepository.save(new GadgetCollectionEntity(
                    newGadget.brawlStarsId(),
                    brawler.getId()
            ));

            newGadget.names().messages().forEach((language, name) -> messageRepository.save(
                    new MessageCollectionEntity(gadget.getNameMessageCode(), language, name)));
        });

        newBrawler.starPowers().forEach(newStarPower -> {
            newStarPower.names().validate();
            StarPowerCollectionEntity starPower = starPowerRepository.save(new StarPowerCollectionEntity(
                    newStarPower.brawlStarsId(),
                    brawler.getId()
            ));

            newStarPower.names().messages().forEach((language, name) -> messageRepository.save(
                    new MessageCollectionEntity(starPower.getNameMessageCode(), language, name)));
        });
    }

    private void validateNewBrawler(NewBrawler newBrawler) {
        if (brawlerRepository.findByBrawlStarsId(newBrawler.brawlStarsId()).isPresent()) {
            throw new AdminException(AdminErrorKind.DUPLICATED,
                    "이미 등록된 브롤러입니다. brawlStarsId: " + newBrawler.brawlStarsId());
        }
        List<GadgetCollectionEntity> existingGadgets = gadgetRepository.findAllByBrawlStarsIdIn(
                newBrawler.gadgets().stream().map(NewGadget::brawlStarsId).toList());
        if (!existingGadgets.isEmpty()) {
            throw new AdminException(AdminErrorKind.DUPLICATED,
                    "이미 등록된 가젯이 포함되어 있습니다. brawlStarsIds: " +
                            existingGadgets.stream().map(GadgetCollectionEntity::getBrawlStarsId).toList());
        }
        List<StarPowerCollectionEntity> existingStarPowers = starPowerRepository.findAllByBrawlStarsIdIn(
                newBrawler.starPowers().stream().map(NewStarPower::brawlStarsId).toList());
        if (!existingStarPowers.isEmpty()) {
            throw new AdminException(AdminErrorKind.DUPLICATED,
                    "이미 등록된 스타파워가 포함되어 있습니다. brawlStarsIds: " +
                            existingStarPowers.stream().map(StarPowerCollectionEntity::getBrawlStarsId).toList());
        }
    }

    @Transactional
    public void update(long brawlStarsId, BrawlerUpdate brawlerUpdate) {
        brawlerUpdate.names().validate();
        BrawlerCollectionEntity brawler = brawlerRepository.findByBrawlStarsId(brawlStarsId)
                .orElseThrow(() -> new AdminException(AdminErrorKind.NOT_FOUND,
                        "브롤러를 찾을 수 없습니다. brawlStarsId: " + brawlStarsId));

        Map<Language, MessageCollectionEntity> langToMessage = messageRepository.findAllByCode(brawler.getNameMessageCode())
                .stream()
                .collect(toMap(MessageCollectionEntity::getLang, m -> m));

        brawlerUpdate.names().messages().forEach((language, name) -> {
            if (langToMessage.containsKey(language)) {
                langToMessage.get(language).update(name);
            } else {
                messageRepository.save(
                        new MessageCollectionEntity(brawler.getNameMessageCode(), language, name));
            }
        });
    }

    public void uploadProfileImage(long brawlStarsId, Resource resource) {
        brawlStarsImageUploader.uploadBrawlerProfile(brawlStarsId, resource);
    }

    public List<Gear> getGearList() {
        List<GearCollectionEntity> gears = gearRepository.findAll();
        Map<String, List<MessageCollectionEntity>> codeToMessages = messageRepository.findAll()
                .stream()
                .collect(groupingBy(MessageCollectionEntity::getCode));

        return gears.stream().map(gear ->
                new Gear(
                        gear,
                        codeToMessages.get(gear.getNameMessageCode())
                )
        ).toList();
    }

    @Transactional
    public void registerGear(NewGear newGear) {
        newGear.names().validate();
        GearCollectionEntity gear = gearRepository.save(
                new GearCollectionEntity(
                        newGear.brawlStarsId(),
                        newGear.rarity()
                )
        );

        newGear.names().messages().forEach((language, name) -> messageRepository.save(
                new MessageCollectionEntity(gear.getNameMessageCode(), language, name)));
    }
}
