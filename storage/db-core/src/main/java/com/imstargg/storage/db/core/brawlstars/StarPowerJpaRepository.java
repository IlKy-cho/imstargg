package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StarPowerJpaRepository extends JpaRepository<StarPowerEntity, Long> {
    List<StarPowerEntity> findAllByBrawlerId(long id);
}
