package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.BrawlStarsImageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrawlStarsImageCollectionJpaRepository extends JpaRepository<BrawlStarsImageCollectionEntity, Long> {

    Optional<BrawlStarsImageCollectionEntity> findByCode(String code);

    List<BrawlStarsImageCollectionEntity> findAllByType(BrawlStarsImageType type);
}
