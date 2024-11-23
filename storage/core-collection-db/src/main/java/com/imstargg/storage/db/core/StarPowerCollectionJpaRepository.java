package com.imstargg.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StarPowerCollectionJpaRepository extends JpaRepository<StarPowerCollectionEntity, Long> {

    Optional<StarPowerCollectionEntity> findByBrawlStarsId(long brawlStarsId);
}
