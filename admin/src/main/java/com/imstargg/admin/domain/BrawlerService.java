package com.imstargg.admin.domain;

import com.imstargg.admin.support.error.AdminErrorKind;
import com.imstargg.admin.support.error.AdminException;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsImageType;
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
        Map<Long, List<GadgetCollectionEntity>> brawlerBrawlStarsIdToGadgets = gadgetRepository.findAll().stream()
                .collect(groupingBy(GadgetCollectionEntity::getBrawlerBrawlStarsId));
        Map<Long, List<StarPowerCollectionEntity>> brawlerBrawlStarsIdToStarPowers = starPowerRepository.findAll().stream()
                .collect(groupingBy(StarPowerCollectionEntity::getBrawlerBrawlStarsId));
        Map<Long, GearCollectionEntity> brawlStarsIdToGear = gearRepository.findAll().stream()
                .collect(toMap(GearCollectionEntity::getBrawlStarsId, Function.identity()));
        Map<Long, List<GearCollectionEntity>> brawlerBrawlStarsIdToGears = brawlerGearRepository.findAll().stream()
                .collect(groupingBy(BrawlerGearCollectionEntity::getBrawlerBrawlStarsId,
                        mapping(
                                brawlerGear -> brawlStarsIdToGear.get(brawlerGear.getGearBrawlStarsId()),
                                toList()
                        )
                ));
        Map<String, BrawlStarsImageCollectionEntity> codeToBrawlerProfileImage = brawlStarsImageRepository.findAllByType(BrawlStarsImageType.BRAWLER_PROFILE)
                .stream()
                .collect(toMap(BrawlStarsImageCollectionEntity::getCode, Function.identity()));

        Map<String, BrawlStarsImageCollectionEntity> codeToGadgetImage = brawlStarsImageRepository.findAllByType(BrawlStarsImageType.GADGET)
                .stream()
                .collect(toMap(BrawlStarsImageCollectionEntity::getCode, Function.identity()));

        Map<String, BrawlStarsImageCollectionEntity> codeToStarPowerImage = brawlStarsImageRepository.findAllByType(BrawlStarsImageType.STAR_POWER)
                .stream()
                .collect(toMap(BrawlStarsImageCollectionEntity::getCode, Function.identity()));

        Map<String, BrawlStarsImageCollectionEntity> codeToGearImage = brawlStarsImageRepository.findAllByType(BrawlStarsImageType.GEAR)
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
                                BrawlStarsImageType.BRAWLER_PROFILE.code(brawler.getBrawlStarsId()), null
                        ),
                        brawlerBrawlStarsIdToGadgets.getOrDefault(brawler.getBrawlStarsId(), List.of()).stream()
                                .map(gadget ->
                                        new Gadget(gadget, codeToMessages.get(gadget.getNameMessageCode()),
                                                codeToGadgetImage.get(BrawlStarsImageType.GADGET.code(gadget.getBrawlStarsId())))
                                ).toList(),
                        brawlerBrawlStarsIdToStarPowers.getOrDefault(brawler.getBrawlStarsId(), List.of()).stream()
                                .map(starPower ->
                                        new StarPower(starPower, codeToMessages.get(starPower.getNameMessageCode()),
                                                codeToStarPowerImage.get(BrawlStarsImageType.STAR_POWER.code(starPower.getBrawlStarsId())))
                                ).toList(),
                        brawlerBrawlStarsIdToGears.getOrDefault(brawler.getBrawlStarsId(), List.of()).stream()
                                .map(gear ->
                                        new Gear(gear, codeToMessages.get(gear.getNameMessageCode()),
                                                codeToGearImage.get(BrawlStarsImageType.GEAR.code(gear.getBrawlStarsId())))
                                ).toList()
                )
        ).toList();
    }

    @Transactional
    public void register(NewBrawler newBrawler) {
        if (brawlerRepository.findByBrawlStarsId(newBrawler.brawlStarsId()).isPresent()) {
            throw new AdminException(AdminErrorKind.DUPLICATED,
                    "이미 등록된 브롤러입니다. brawlStarsId: " + newBrawler.brawlStarsId());
        }
        newBrawler.names().validate();
        BrawlerCollectionEntity brawler = brawlerRepository.save(new BrawlerCollectionEntity(
                newBrawler.brawlStarsId(),
                newBrawler.rarity(),
                newBrawler.role()
        ));

        newBrawler.names().messages().forEach((language, name) -> messageRepository.save(
                new MessageCollectionEntity(brawler.getNameMessageCode(), language, name)));
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

        Map<String, BrawlStarsImageCollectionEntity> codeToGearImage = brawlStarsImageRepository.findAllByType(BrawlStarsImageType.GEAR)
                .stream()
                .collect(toMap(BrawlStarsImageCollectionEntity::getCode, Function.identity()));

        return gears.stream().map(gear ->
                new Gear(
                        gear,
                        codeToMessages.get(gear.getNameMessageCode()),
                        codeToGearImage.get(BrawlStarsImageType.GEAR.code(gear.getBrawlStarsId()))
                )
        ).toList();
    }

    @Transactional
    public void registerGear(NewGear newGear) {
        if (gearRepository.findByBrawlStarsId(newGear.brawlStarsId()).isPresent()) {
            throw new AdminException(AdminErrorKind.DUPLICATED,
                    "이미 등록된 기어입니다. brawlStarsId: " + newGear.brawlStarsId());
        }
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

    @Transactional
    public void updateGear(long brawlStarsId, GearUpdate gearUpdate) {
        gearUpdate.names().validate();
        GearCollectionEntity gear = gearRepository.findByBrawlStarsId(brawlStarsId)
                .orElseThrow(() -> new AdminException(AdminErrorKind.NOT_FOUND,
                        "기어를 찾을 수 없습니다. brawlStarsId: " + brawlStarsId));

        Map<Language, MessageCollectionEntity> langToMessage = messageRepository.findAllByCode(gear.getNameMessageCode())
                .stream()
                .collect(toMap(MessageCollectionEntity::getLang, m -> m));

        gearUpdate.names().messages().forEach((language, name) -> {
            if (langToMessage.containsKey(language)) {
                langToMessage.get(language).update(name);
            } else {
                messageRepository.save(
                        new MessageCollectionEntity(gear.getNameMessageCode(), language, name));
            }
        });
    }

    @Transactional
    public void registerBrawlerGear(long brawlerBrawlStarsId, NewBrawlerGear newBrawlerGear) {
        BrawlerCollectionEntity brawler = brawlerRepository.findByBrawlStarsId(brawlerBrawlStarsId)
                .orElseThrow(() -> new AdminException(AdminErrorKind.NOT_FOUND,
                        "브롤러를 찾을 수 없습니다. brawlStarsId: " + brawlerBrawlStarsId));
        GearCollectionEntity gear = gearRepository.findByBrawlStarsId(newBrawlerGear.brawlStarsId())
                .orElseThrow(() -> new AdminException(AdminErrorKind.NOT_FOUND,
                        "기어를 찾을 수 없습니다. brawlStarsId: " + newBrawlerGear.brawlStarsId())
                );
        BrawlerGearCollectionEntity brawlerGear = brawlerGearRepository.findByBrawlerBrawlStarsIdAndGearBrawlStarsId(
                brawler.getBrawlStarsId(), gear.getBrawlStarsId()
        ).orElseGet(() -> new BrawlerGearCollectionEntity(
                brawler.getBrawlStarsId(), gear.getBrawlStarsId()
        ));
        brawlerGear.restore();
        brawlerGearRepository.save(brawlerGear);
    }

    @Transactional
    public void registerStarPower(long brawlerBrawlStarsId, NewStarPower newStarPower) {
        if (starPowerRepository.findByBrawlStarsId(newStarPower.brawlStarsId()).isPresent()) {
            throw new AdminException(AdminErrorKind.DUPLICATED,
                    "이미 등록된 스타파워입니다. brawlStarsId: " + newStarPower.brawlStarsId());
        }
        if (brawlerRepository.findByBrawlStarsId(brawlerBrawlStarsId).isEmpty()) {
            throw new AdminException(AdminErrorKind.NOT_FOUND,
                    "브롤러를 찾을 수 없습니다. brawlStarsId: " + brawlerBrawlStarsId);
        }
        newStarPower.names().validate();
        StarPowerCollectionEntity starPower = starPowerRepository.save(new StarPowerCollectionEntity(
                newStarPower.brawlStarsId(),
                brawlerBrawlStarsId
        ));

        newStarPower.names().messages().forEach((language, name) -> messageRepository.save(
                new MessageCollectionEntity(starPower.getNameMessageCode(), language, name)));
    }

    @Transactional
    public void registerGadget(long brawlerBrawlStarsId, NewGadget newGadget) {
        if (gadgetRepository.findByBrawlStarsId(newGadget.brawlStarsId()).isPresent()) {
            throw new AdminException(AdminErrorKind.DUPLICATED,
                    "이미 등록된 가젯입니다. brawlStarsId: " + newGadget.brawlStarsId());
        }
        if (brawlerRepository.findByBrawlStarsId(brawlerBrawlStarsId).isEmpty()) {
            throw new AdminException(AdminErrorKind.NOT_FOUND,
                    "브롤러를 찾을 수 없습니다. brawlStarsId: " + brawlerBrawlStarsId);
        }
        newGadget.names().validate();
        GadgetCollectionEntity gadget = gadgetRepository.save(new GadgetCollectionEntity(
                newGadget.brawlStarsId(),
                brawlerBrawlStarsId
        ));

        newGadget.names().messages().forEach((language, name) -> messageRepository.save(
                new MessageCollectionEntity(gadget.getNameMessageCode(), language, name)));
    }

    public void uploadGearImage(long brawlStarsId, Resource resource) {
        brawlStarsImageUploader.uploadGear(brawlStarsId, resource);
    }

    public void uploadGadgetImage(long brawlStarsId, Resource resource) {
        brawlStarsImageUploader.uploadGadget(brawlStarsId, resource);
    }

    public void uploadStarPowerImage(long brawlStarsId, Resource resource) {
        brawlStarsImageUploader.uploadStarPower(brawlStarsId, resource);
    }
}
