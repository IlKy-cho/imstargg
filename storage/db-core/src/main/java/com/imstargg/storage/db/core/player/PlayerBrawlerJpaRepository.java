package com.imstargg.storage.db.core.player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerBrawlerJpaRepository extends JpaRepository<PlayerBrawlerEntity, Long> {

    List<PlayerBrawlerEntity> findAllByPlayerId(long playerId);
}
