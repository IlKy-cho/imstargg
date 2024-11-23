package com.imstargg.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GadgetCollectionJpaRepository extends JpaRepository<GadgetCollectionEntity, Long> {

    Optional<GadgetCollectionEntity> findByBrawlStarsId(long brawlStarsId);
}
