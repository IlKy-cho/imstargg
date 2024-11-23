package com.imstargg.batch.domain;

import com.imstargg.batch.support.AbstractInMemoryCacheRepository;
import com.imstargg.storage.db.core.GadgetCollectionEntity;
import com.imstargg.storage.db.core.GadgetCollectionJpaRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Lazy
@Component
public class GadgetBrawlStarsKeyRepositoryInMemoryCache extends AbstractInMemoryCacheRepository<Long, GadgetCollectionEntity> {

    private final GadgetCollectionJpaRepository gadgetJpaRepository;

    public GadgetBrawlStarsKeyRepositoryInMemoryCache(GadgetCollectionJpaRepository gadgetJpaRepository) {
        super(gadgetJpaRepository::findAll);
        this.gadgetJpaRepository = gadgetJpaRepository;
    }

    @Override
    protected Long key(GadgetCollectionEntity value) {
        return value.getBrawlStarsId();
    }

    @Override
    protected Optional<GadgetCollectionEntity> findData(Long key) {
        return gadgetJpaRepository.findByBrawlStarsId(key);
    }

    @Override
    protected GadgetCollectionEntity saveData(GadgetCollectionEntity value) {
        return gadgetJpaRepository.save(value);
    }
}
