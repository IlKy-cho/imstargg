package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class NewPlayerService {

    private final Clock clock;
    private final UnknownPlayerCollectionJpaRepository unknownPlayerRepository;

    public NewPlayerService(
            Clock clock,
            UnknownPlayerCollectionJpaRepository unknownPlayerRepository
    ) {
        this.clock = clock;
        this.unknownPlayerRepository = unknownPlayerRepository;
    }

    public void create(String brawlStarsTag) {
        unknownPlayerRepository.save(
                UnknownPlayerCollectionEntity.adminNew(brawlStarsTag, clock)
        );
    }
}
