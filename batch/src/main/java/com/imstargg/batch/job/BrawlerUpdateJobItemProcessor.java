package com.imstargg.batch.job;

import com.imstargg.batch.domain.BrawlerBrawlStarsKeyRepositoryInMemoryCache;
import com.imstargg.batch.domain.GadgetBrawlStarsKeyRepositoryInMemoryCache;
import com.imstargg.batch.domain.StarPowerBrawlStarsKeyRepositoryInMemoryCache;
import com.imstargg.client.brawlstars.response.BrawlerResponse;
import com.imstargg.storage.db.core.BrawlerCollectionEntity;
import com.imstargg.storage.db.core.GadgetCollectionEntity;
import com.imstargg.storage.db.core.StarPowerCollectionEntity;
import org.springframework.batch.item.ItemProcessor;

public class BrawlerUpdateJobItemProcessor implements ItemProcessor<BrawlerResponse, BrawlerCollectionEntity> {

    private final BrawlerBrawlStarsKeyRepositoryInMemoryCache brawlerRepository;
    private final GadgetBrawlStarsKeyRepositoryInMemoryCache gadgetRepository;
    private final StarPowerBrawlStarsKeyRepositoryInMemoryCache starPowerRepository;

    public BrawlerUpdateJobItemProcessor(
            BrawlerBrawlStarsKeyRepositoryInMemoryCache brawlerRepository,
            GadgetBrawlStarsKeyRepositoryInMemoryCache gadgetRepository,
            StarPowerBrawlStarsKeyRepositoryInMemoryCache starPowerRepository) {
        this.brawlerRepository = brawlerRepository;
        this.gadgetRepository = gadgetRepository;
        this.starPowerRepository = starPowerRepository;
    }

    @Override
    public BrawlerCollectionEntity process(BrawlerResponse item) throws Exception {
        item.gadgets().stream()
                .filter(gadget -> gadgetRepository.find(gadget.id()).isEmpty())
                .forEach(gadget -> gadgetRepository.save(
                        new GadgetCollectionEntity(gadget.id(), gadget.name())
                ));
        item.starPowers().stream()
                .filter(starPower -> starPowerRepository.find(starPower.id()).isEmpty())
                .forEach(starPower -> starPowerRepository.save(
                        new StarPowerCollectionEntity(starPower.id(), starPower.name())
                ));

        BrawlerCollectionEntity brawler = brawlerRepository.find(item.id())
                .orElse(new BrawlerCollectionEntity(item.id(), item.name()));
        long addedGadgetCount = item.gadgets().stream()
                .filter(gadget -> brawler.addGadgetId(gadget.id()))
                .count();
        long addedStarPowerCount = item.starPowers().stream()
                .filter(starPower -> brawler.addStarPowerId(starPower.id()))
                .count();

        return addedGadgetCount + addedStarPowerCount > 0 ? brawler : null;
    }
}
