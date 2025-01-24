package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.enums.Language;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrawlStarsNewsService {

    private final BrawlStarsNewsReaderWithCache brawlStarsNewsReader;

    public BrawlStarsNewsService(BrawlStarsNewsReaderWithCache brawlStarsNewsReader) {
        this.brawlStarsNewsReader = brawlStarsNewsReader;
    }

    public List<BrawlStarsNews> getNews(Language language) {
        return brawlStarsNewsReader.read(language);
    }
}
