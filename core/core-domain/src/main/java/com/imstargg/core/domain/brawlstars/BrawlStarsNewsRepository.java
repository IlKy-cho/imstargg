package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.Slice;
import com.imstargg.core.enums.Language;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsNewsJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class BrawlStarsNewsRepository {

    private final BrawlStarsNewsJpaRepository brawlStarsNewsJpaRepository;

    public BrawlStarsNewsRepository(BrawlStarsNewsJpaRepository brawlStarsNewsJpaRepository) {
        this.brawlStarsNewsJpaRepository = brawlStarsNewsJpaRepository;
    }

    public Slice<BrawlStarsNews> find(Language language, BrawlStarsNewsPageParam pageParam) {
        org.springframework.data.domain.Slice<BrawlStarsNews> jpaSlice = brawlStarsNewsJpaRepository
                .findAllByLanguageAndOrderByPublishDateDesc(
                        language.getCode(),
                        pageParam.page() - 1,
                        pageParam.size()
                ).map(entity -> new BrawlStarsNews(
                        entity.getTitle(),
                        entity.getLinkUrl(),
                        entity.getPublishDate()
                ));

        return new Slice<>(
                jpaSlice.getContent(),
                jpaSlice.hasNext()
        );
    }
}
