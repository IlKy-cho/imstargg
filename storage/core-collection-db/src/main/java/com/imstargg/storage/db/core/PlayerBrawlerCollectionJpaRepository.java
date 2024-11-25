package com.imstargg.storage.db.core;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerBrawlerCollectionJpaRepository extends JpaRepository<PlayerBrawlerCollectionEntity, Long> {

    List<PlayerBrawlerCollectionEntity> findAllByPlayerIdIn(List<Long> playerIds);
}
