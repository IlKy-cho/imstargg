package com.imstargg.storage.db.core.cache;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RdsCacheJpaRepository extends JpaRepository<RdsCacheEntity, Long> {

    Optional<RdsCacheEntity> findByKey(String key);
}
