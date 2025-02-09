package com.imstargg.core.domain.statistics.brawler;

import com.imstargg.core.domain.brawlstars.Brawler;
import com.imstargg.core.domain.brawlstars.Gadget;
import com.imstargg.core.domain.brawlstars.Gear;
import com.imstargg.core.domain.brawlstars.StarPower;
import com.imstargg.storage.db.core.statistics.BrawlerCountEntity;
import com.imstargg.storage.db.core.statistics.BrawlerCountJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerItemCountEntity;
import com.imstargg.storage.db.core.statistics.BrawlerItemCountJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class BrawlerCountRepository {

    private final BrawlerCountJpaRepository brawlerCountJpaRepository;
    private final BrawlerItemCountJpaRepository brawlerItemCountJpaRepository;

    public BrawlerCountRepository(
            BrawlerCountJpaRepository brawlerCountJpaRepository,
            BrawlerItemCountJpaRepository brawlerItemCountJpaRepository
    ) {
        this.brawlerCountJpaRepository = brawlerCountJpaRepository;
        this.brawlerItemCountJpaRepository = brawlerItemCountJpaRepository;
    }

    public int getBrawlerCount(Brawler brawler) {
        return brawlerCountJpaRepository.findByBrawlerBrawlStarsId(brawler.id().value())
                .map(BrawlerCountEntity::getCount)
                .orElse(0);
    }

    public int getGadgetCount(Brawler brawler, Gadget gadget) {
        return brawlerItemCountJpaRepository.findByBrawlerBrawlStarsIdAndItemBrawlStarsId(
                        brawler.id().value(), gadget.id().value())
                .map(BrawlerItemCountEntity::getCount)
                .orElse(0);
    }

    public int getStarPowerCount(Brawler brawler, StarPower starPower) {
        return brawlerItemCountJpaRepository.findByBrawlerBrawlStarsIdAndItemBrawlStarsId(
                        brawler.id().value(), starPower.id().value())
                .map(BrawlerItemCountEntity::getCount)
                .orElse(0);
    }

    public int getGearCount(Brawler brawler, Gear gear) {
        return brawlerItemCountJpaRepository.findByBrawlerBrawlStarsIdAndItemBrawlStarsId(
                        brawler.id().value(), gear.id().value())
                .map(BrawlerItemCountEntity::getCount)
                .orElse(0);
    }
}
