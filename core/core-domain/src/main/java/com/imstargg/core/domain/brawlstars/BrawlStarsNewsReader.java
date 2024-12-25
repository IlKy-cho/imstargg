package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class BrawlStarsNewsReader {

    private final BrawlStarsNewsRepository brawlStarsNewsRepository;

    public BrawlStarsNewsReader(BrawlStarsNewsRepository brawlStarsNewsRepository) {
        this.brawlStarsNewsRepository = brawlStarsNewsRepository;
    }

    public Slice<BrawlStarsNews> read(BrawlStarsNewsPageParam pageParam) {
        return brawlStarsNewsRepository.find(pageParam.language(), pageParam);
    }
}
