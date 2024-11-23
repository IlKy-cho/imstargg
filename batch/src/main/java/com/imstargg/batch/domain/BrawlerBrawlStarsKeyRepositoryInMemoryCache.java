package com.imstargg.batch.domain;

import com.imstargg.batch.support.AbstractInMemoryCacheRepository;
import com.imstargg.storage.db.core.BrawlerCollectionEntity;
import com.imstargg.storage.db.core.BrawlerCollectionJpaRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Lazy
@Component
public class BrawlerBrawlStarsKeyRepositoryInMemoryCache
        extends AbstractInMemoryCacheRepository<Long, BrawlerCollectionEntity> {

    private final BrawlerCollectionJpaRepository brawlerJpaRepository;

    public BrawlerBrawlStarsKeyRepositoryInMemoryCache(
            BrawlerCollectionJpaRepository brawlerJpaRepository) {
        super(brawlerJpaRepository::findAll);
        this.brawlerJpaRepository = brawlerJpaRepository;
    }

    @Override
    protected Long key(BrawlerCollectionEntity value) {
        return value.getBrawlStarsId();
    }

    @Override
    protected Optional<BrawlerCollectionEntity> findData(Long key) {
        return brawlerJpaRepository.findByBrawlStarsId(key);
    }

    @Override
    protected BrawlerCollectionEntity saveData(BrawlerCollectionEntity value) {
        return brawlerJpaRepository.save(value);
    }
}
