package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrawlStarsImageJpaRepository extends JpaRepository<BrawlStarsImageEntity, Long> {

    Optional<BrawlStarsImageEntity> findByCode(String code);
}
