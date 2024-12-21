package com.imstargg.storage.db.core.brawlstars;

import com.imstargg.core.enums.Language;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrawlStarsNewsJpaRepository extends JpaRepository<BrawlStarsNewsEntity, Long> {

    Slice<BrawlStarsNewsEntity> findAllByLanguageOrderByPublishDateDesc(Language language, Pageable pageable);
}
