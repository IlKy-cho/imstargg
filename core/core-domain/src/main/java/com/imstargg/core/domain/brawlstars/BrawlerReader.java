package com.imstargg.core.domain.brawlstars;

import com.imstargg.core.domain.BrawlStarsId;
import com.imstargg.core.enums.Language;
import com.imstargg.core.error.CoreErrorType;
import com.imstargg.core.error.CoreException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BrawlerReader {

    private final BrawlerRepository brawlerRepository;
    private final BrawlerRepositoryWithCache brawlerRepositoryWithCache;

    public BrawlerReader(
            BrawlerRepository brawlerRepository,
            BrawlerRepositoryWithCache brawlerRepositoryWithCache
    ) {
        this.brawlerRepository = brawlerRepository;
        this.brawlerRepositoryWithCache = brawlerRepositoryWithCache;
    }

    public List<Brawler> getAll(Language language) {
        return brawlerRepository.findAllIds().stream()
                .map(id -> brawlerRepositoryWithCache.find(id, language))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public Brawler get(BrawlStarsId id, Language language) {
        return brawlerRepositoryWithCache.find(id, language)
                .orElseThrow(() -> new CoreException(CoreErrorType.BRAWLER_NOT_FOUND, "id=" + id));
    }
}
