package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.brawlstars.Brawler;
import org.springframework.stereotype.Component;

@Component
public class BrawlerOwnershipReader {

    private final BrawlerOwnershipRateRepositoryWithCache brawlerOwnershipRateRepository;

    public BrawlerOwnershipReader(BrawlerOwnershipRateRepositoryWithCache brawlerOwnershipRateRepository) {
        this.brawlerOwnershipRateRepository = brawlerOwnershipRateRepository;
    }

    public BrawlerOwnershipRate get(Brawler brawler) {
        return brawlerOwnershipRateRepository.get(brawler);
    }
}
