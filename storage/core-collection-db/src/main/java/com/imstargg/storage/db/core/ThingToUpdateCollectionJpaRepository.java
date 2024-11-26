package com.imstargg.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThingToUpdateCollectionJpaRepository extends JpaRepository<ThingToUpdateCollectionEntity, Long> {

    List<ThingToUpdateCollectionEntity> findAllByDeletedFalse();
}
