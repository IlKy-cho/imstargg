package com.imstargg.storage.db.core.player;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BattleCollectionJpaRepository extends JpaRepository<BattleCollectionEntity, Long> {

    Optional<BattleCollectionEntity> findFirst1ByOrderByIdDesc();
}
