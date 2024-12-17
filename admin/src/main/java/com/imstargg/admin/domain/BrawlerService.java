package com.imstargg.admin.domain;

import com.imstargg.admin.support.error.AdminErrorKind;
import com.imstargg.admin.support.error.AdminException;
import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlerCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlerCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.GadgetCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.GadgetCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.StarPowerCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.StarPowerCollectionJpaRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BrawlerService {

    private final BrawlerCollectionJpaRepository brawlerRepository;
    private final GadgetCollectionJpaRepository gadgetRepository;
    private final StarPowerCollectionJpaRepository starPowerRepository;
    private final MessageCollectionJpaRepository messageRepository;
    private final BrawlStarsImageUploader brawlStarsImageUploader;

    public BrawlerService(
            BrawlerCollectionJpaRepository brawlerRepository,
            GadgetCollectionJpaRepository gadgetRepository,
            StarPowerCollectionJpaRepository starPowerRepository,
            MessageCollectionJpaRepository messageRepository,
            BrawlStarsImageUploader brawlStarsImageUploader
    ) {
        this.brawlerRepository = brawlerRepository;
        this.gadgetRepository = gadgetRepository;
        this.starPowerRepository = starPowerRepository;
        this.messageRepository = messageRepository;
        this.brawlStarsImageUploader = brawlStarsImageUploader;
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
                new MessageCollectionEntity(brawler.getNameMessageCode(), language.getCode(), name)));

        newBrawler.gadgets().forEach(newGadget -> {
            newGadget.names().validate();
            GadgetCollectionEntity gadget = gadgetRepository.save(new GadgetCollectionEntity(
                    newGadget.brawlStarsId(),
                    brawler.getId()
            ));

            newGadget.names().messages().forEach((language, name) -> messageRepository.save(
                    new MessageCollectionEntity(gadget.getNameMessageCode(), language.getCode(), name)));
        });

        newBrawler.starPowers().forEach(newStarPower -> {
            newStarPower.names().validate();
            StarPowerCollectionEntity starPower = starPowerRepository.save(new StarPowerCollectionEntity(
                    newStarPower.brawlStarsId(),
                    brawler.getId()
            ));

            newStarPower.names().messages().forEach((language, name) -> messageRepository.save(
                    new MessageCollectionEntity(starPower.getNameMessageCode(), language.getCode(), name)));
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

        Map<String, MessageCollectionEntity> langToMessage = messageRepository.findAllByCode(brawler.getNameMessageCode())
                        .stream()
                                .collect(Collectors.toMap(MessageCollectionEntity::getLang, m -> m));

        brawlerUpdate.names().messages().forEach((language, name) -> {
            if (langToMessage.containsKey(language.getCode())) {
                langToMessage.get(language.getCode()).update(name);
            } else {
                messageRepository.save(
                        new MessageCollectionEntity(brawler.getNameMessageCode(), language.getCode(), name));
            }
        });
    }

    public void uploadProfileImage(long brawlStarsId, Resource resource) {
        brawlStarsImageUploader.uploadBrawlerProfile(brawlStarsId, resource);
    }
}
