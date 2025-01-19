package com.imstargg.batch.job.statistics;

import com.imstargg.batch.domain.BrawlerBattleResultStatisticsCollector;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlerBattleResultStatisticsCollectionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public class BrawlerBattleResultStatisticsJobItemProcessor
        implements ItemProcessor<Long, List<BrawlerBattleResultStatisticsCollectionEntity>> {

    private static final Logger log = LoggerFactory.getLogger(BrawlerBattleResultStatisticsJobItemProcessor.class);

    private static final int PAGE_SIZE = 1000;

    private final EntityManagerFactory emf;
    private final BattleCollectionJpaRepository battleCollectionJpaRepository;
    private final Clock clock;
    private final LocalDate date;

    public BrawlerBattleResultStatisticsJobItemProcessor(
            EntityManagerFactory emf,
            BattleCollectionJpaRepository battleCollectionJpaRepository,
            Clock clock,
            LocalDate date
    ) {
        this.emf = emf;
        this.battleCollectionJpaRepository = battleCollectionJpaRepository;
        this.clock = clock;
        this.date = date;
    }

    @Override
    public List<BrawlerBattleResultStatisticsCollectionEntity> process(Long item) throws Exception {
        OffsetDateTime fromBattleTime = date.atStartOfDay(clock.getZone()).toOffsetDateTime();
        OffsetDateTime toBattleTime = date.plusDays(1).atStartOfDay(clock.getZone()).toOffsetDateTime();
        long eventId = item;
        var collector = new BrawlerBattleResultStatisticsCollector(emf, date, item);
        boolean hasNext = true;
        var pageRequest = PageRequest.ofSize(PAGE_SIZE);
        while (hasNext) {
            log.debug("Reading battles. page: {}, eventId: {}, date: {}", pageRequest.getPageNumber(), eventId, date);
            Slice<BattleCollectionEntity> slice = battleCollectionJpaRepository
                    .findSliceWithPlayerByEventBrawlStarsIdAndBattleTimeGreaterThanEqualAndBattleTimeLessThan(
                            eventId, fromBattleTime, toBattleTime, pageRequest
                    );
            hasNext = slice.hasNext();
            pageRequest = pageRequest.next();

            List<BattleCollectionEntity> battles = slice.getContent();
            log.debug("Processing {} battles. eventId: {}, date: {}", battles.size(), eventId, date);
            for (BattleCollectionEntity battle : battles) {
                if (battle.canResultStatisticsCollected()) {
                    collector.collect(battle);
                }
            }
        }

        return collector.result();
    }

}
