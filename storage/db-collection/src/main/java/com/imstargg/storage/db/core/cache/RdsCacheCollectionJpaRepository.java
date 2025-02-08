package com.imstargg.storage.db.core.cache;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RdsCacheCollectionJpaRepository extends JpaRepository<RdsCacheCollectionEntity, Long> {

    Optional<RdsCacheCollectionEntity> findByKey(String key);

    List<RdsCacheCollectionEntity> findAllByKeyIn(List<String> keys);
}
