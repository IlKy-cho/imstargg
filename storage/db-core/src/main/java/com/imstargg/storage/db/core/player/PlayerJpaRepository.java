package com.imstargg.storage.db.core.player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerJpaRepository extends JpaRepository<PlayerEntity, Long> {

    Optional<PlayerEntity> findByBrawlStarsTagAndDeletedFalse(String brawlStarsTag);

    List<PlayerEntity> findAllByNameAndDeletedFalse(String name);

}
