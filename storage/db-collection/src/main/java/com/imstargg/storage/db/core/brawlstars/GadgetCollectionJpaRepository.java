package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GadgetCollectionJpaRepository extends JpaRepository<GadgetCollectionEntity, Long> {

    List<GadgetCollectionEntity> findAllByBrawlStarsIdIn(List<Long> brawlStarsIds);

    Optional<GadgetCollectionEntity> findByBrawlStarsId(Long brawlStarsId);
} 