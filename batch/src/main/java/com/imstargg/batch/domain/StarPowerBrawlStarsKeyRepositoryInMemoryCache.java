package com.imstargg.batch.domain;

import com.imstargg.batch.support.AbstractInMemoryCacheRepository;
import com.imstargg.storage.db.core.StarPowerCollectionEntity;
import com.imstargg.storage.db.core.StarPowerCollectionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StarPowerBrawlStarsKeyRepositoryInMemoryCache
        extends AbstractInMemoryCacheRepository<Long, StarPowerCollectionEntity> {

    private final StarPowerCollectionJpaRepository starPowerJpaRepository;

    public StarPowerBrawlStarsKeyRepositoryInMemoryCache(
            StarPowerCollectionJpaRepository starPowerJpaRepository) {
        super(starPowerJpaRepository::findAll);
        this.starPowerJpaRepository = starPowerJpaRepository;
    }

    @Override
    protected Long key(StarPowerCollectionEntity value) {
        return value.getBrawlStarsId();
    }

    @Override
    protected Optional<StarPowerCollectionEntity> findData(Long key) {
        return starPowerJpaRepository.findByBrawlStarsId(key);
    }

    @Override
    protected StarPowerCollectionEntity saveData(StarPowerCollectionEntity value) {
        return starPowerJpaRepository.save(value);
    }
}
