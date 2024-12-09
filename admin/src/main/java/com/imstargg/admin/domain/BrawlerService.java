package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.MessageCollectionEntity;
import com.imstargg.storage.db.core.MessageCollectionJpaRepository;
import com.imstargg.storage.db.core.brawlstars.BrawlerCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlerCollectionJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        newBrawler.validate();
        BrawlerCollectionEntity brawler = brawlerRepository.save(new BrawlerCollectionEntity(
                newBrawler.brawlStarsId(),
                newBrawler.rarity(),
                newBrawler.role()
        ));

        newBrawler.names().forEach((language, name) -> {
            messageRepository.save(new MessageCollectionEntity(brawler.getNameMessageCode(), language.getCode(), name));
        });
    }
}
