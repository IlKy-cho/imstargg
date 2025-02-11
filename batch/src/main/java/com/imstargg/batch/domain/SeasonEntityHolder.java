package com.imstargg.batch.domain;

import com.imstargg.storage.db.core.brawlstars.BrawlPassSeasonCollectionEntity;
import com.imstargg.storage.db.core.brawlstars.BrawlPassSeasonCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Component
public class SeasonEntityHolder {

    private final BrawlPassSeasonCollectionJpaRepository repository;
    private final ReentrantLock lock = new ReentrantLock();
    private BrawlPassSeasonCollectionEntity entity;

    public SeasonEntityHolder(BrawlPassSeasonCollectionJpaRepository repository) {
        this.repository = repository;
    }

    public BrawlPassSeasonCollectionEntity getCurrentSeasonEntity() {
        if (entity == null) {
            lock.lock();
            try {
                if (entity == null) {
                    entity = repository.getCurrentSeason();
                }
            } finally {
                lock.unlock();
            }
        }
        return entity;
    }
}
