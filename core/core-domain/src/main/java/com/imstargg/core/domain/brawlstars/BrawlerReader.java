package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.Language;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrawlerReader {

    private final BrawlerRepositoryWithCache brawlerRepository;

    public BrawlerReader(
            BrawlerRepositoryWithCache brawlerRepository
    ) {
        this.brawlerRepository = brawlerRepository;
    }

    public List<Brawler> getAll(Language language) {
        return brawlerRepository.findAll(language);
    }

    public Brawler get(BrawlStarsId id, Language language) {
        return brawlerRepository.find(id, language)
                .orElseThrow(() -> new CoreException(CoreErrorType.BRAWLER_NOT_FOUND, "id=" + id));
    }
}
