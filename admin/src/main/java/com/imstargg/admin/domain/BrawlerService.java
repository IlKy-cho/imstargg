package com.imstargg.admin.domain;

import com.imstargg.admin.support.error.AdminErrorKind;
import com.imstargg.admin.support.error.AdminException;
import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlerCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlerCollectionJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BrawlerService {

    private final BrawlerCollectionJpaRepository brawlerRepository;
    private final MessageCollectionJpaRepository messageRepository;

    public BrawlerService(
            BrawlerCollectionJpaRepository brawlerRepository,
            MessageCollectionJpaRepository messageRepository
    ) {
        this.brawlerRepository = brawlerRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void register(NewBrawler newBrawler) {
        newBrawler.names().validate();
        if (brawlerRepository.findByBrawlStarsId(newBrawler.brawlStarsId()).isPresent()) {
            throw new AdminException(AdminErrorKind.DUPLICATED,
                    "이미 등록된 브롤러입니다. brawlStarsId: " + newBrawler.brawlStarsId());
        }
        BrawlerCollectionEntity brawler = brawlerRepository.save(new BrawlerCollectionEntity(
                newBrawler.brawlStarsId(),
                newBrawler.rarity(),
                newBrawler.role()
        ));

        newBrawler.names().messages().forEach((language, name) -> messageRepository.save(
                new MessageCollectionEntity(brawler.getNameMessageCode(), language.getCode(), name)));
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
}
