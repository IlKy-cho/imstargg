package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class BrawlStarsNewsService {

    private final BrawlStarsNewsReaderWithCache brawlStarsNewsReader;

    public BrawlStarsNewsService(BrawlStarsNewsReaderWithCache brawlStarsNewsReader) {
        this.brawlStarsNewsReader = brawlStarsNewsReader;
    }

    public Slice<BrawlStarsNews> getNews(BrawlStarsNewsPageParam pageParam) {
        return brawlStarsNewsReader.read(pageParam);
    }
}
