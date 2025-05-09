package com.imstargg.storage.db.core.player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnknownPlayerJpaRepository extends JpaRepository<UnknownPlayerEntity, Long> {

    Optional<UnknownPlayerEntity> findByBrawlStarsTag(String brawlStarsTag);
}
