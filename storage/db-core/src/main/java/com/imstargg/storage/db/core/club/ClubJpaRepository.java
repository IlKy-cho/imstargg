package com.imstargg.storage.db.core.club;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClubJpaRepository extends JpaRepository<ClubEntity, Long> {

    Optional<ClubEntity> findByBrawlStarsTagAndDeletedFalse(String brawlStarsTag);
}
