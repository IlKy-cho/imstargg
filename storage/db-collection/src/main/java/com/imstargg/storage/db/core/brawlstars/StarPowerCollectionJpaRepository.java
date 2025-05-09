package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StarPowerCollectionJpaRepository extends JpaRepository<StarPowerCollectionEntity, Long> {

    List<StarPowerCollectionEntity> findAllByBrawlStarsIdIn(List<Long> brawlStarsIds);

    Optional<StarPowerCollectionEntity> findByBrawlStarsId(Long brawlStarsId);
} 