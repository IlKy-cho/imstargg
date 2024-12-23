package com.imstargg.admin.domain;

import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.imstargg.storage.db.core.UnknownPlayerCollectionJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewPlayerService {

    private final UnknownPlayerCollectionJpaRepository unknownPlayerRepository;

    public NewPlayerService(
            UnknownPlayerCollectionJpaRepository unknownPlayerRepository
    ) {
        this.unknownPlayerRepository = unknownPlayerRepository;
    }

    @Transactional
    public void create(String brawlStarsTag) {
        unknownPlayerRepository.findWithOptimisticLockByBrawlStarsTag(brawlStarsTag).ifPresentOrElse(
                player -> {
                    player.restore();
                    player.adminNew();
                },
                () -> unknownPlayerRepository.save(
                        UnknownPlayerCollectionEntity.adminNew(brawlStarsTag)
                )
        );
    }
}
