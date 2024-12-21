package com.imstargg.storage.db.core.brawlstars;

import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrawlStarsNewsJpaRepository extends JpaRepository<BrawlStarsNewsEntity, Long> {

    Slice<BrawlStarsNewsEntity> findAllByLanguageAndOrderByPublishDateDesc(String language, int page, int size);
}
