package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrawlStarsImageCollectionJpaRepository extends JpaRepository<BrawlStarsImageCollectionEntity, Long> {

    Optional<BrawlStarsImageCollectionEntity> findByCode(String code);
}
