package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GadgetJpaRepository extends JpaRepository<GadgetEntity, Long> {
    List<GadgetEntity> findAllByBrawlerId(long id);
}
