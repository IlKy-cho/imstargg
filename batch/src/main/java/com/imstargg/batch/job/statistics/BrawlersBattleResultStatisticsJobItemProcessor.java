package com.imstargg.batch.job.statistics;

import com.imstargg.batch.domain.statistics.BrawlersBattleResultStatisticsCollector;
import com.imstargg.storage.db.core.BattleCollectionEntity;
import com.imstargg.storage.db.core.BattleCollectionJpaRepository;
import com.imstargg.storage.db.core.statistics.BrawlersBattleResultStatisticsCollectionEntity;
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

public class BrawlersBattleResultStatisticsJobItemProcessor
        implements ItemProcessor<Long, List<BrawlersBattleResultStatisticsCollectionEntity>> {

    private static final Logger log = LoggerFactory.getLogger(BrawlersBattleResultStatisticsJobItemProcessor.class);

    private static final int PAGE_SIZE = 1000;

    private final EntityManagerFactory emf;
    private final BattleCollectionJpaRepository battleCollectionJpaRepository;
    private final Clock clock;
    private final LocalDate date;

    public BrawlersBattleResultStatisticsJobItemProcessor(
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
    public List<BrawlersBattleResultStatisticsCollectionEntity> process(Long item) throws Exception {
        OffsetDateTime fromBattleTime = date.atStartOfDay(clock.getZone()).toOffsetDateTime();
        OffsetDateTime toBattleTime = date.plusDays(1).atStartOfDay(clock.getZone()).toOffsetDateTime();
        long eventId = item;
        var collector = new BrawlersBattleResultStatisticsCollector(emf, date, item);
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
            int processedCount = 0;
            for (BattleCollectionEntity battle : battles) {
                if (battle.canResultStatisticsCollected()) {
                    collector.collect(battle);
                    processedCount++;
                }
            }
            log.debug("Processed {}/{} battles. eventId: {}, date: {}",
                    processedCount, battles.size(), eventId, date);
        }

        return collector.result();
    }

}
