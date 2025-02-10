package com.imstargg.storage.db.core.brawlstars;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.OffsetDateTime;

import static com.imstargg.storage.db.core.brawlstars.QBrawlPassSeasonCollectionEntity.brawlPassSeasonCollectionEntity;

class BrawlPassSeasonCollectionJpaRepositoryCustomImpl implements BrawlPassSeasonCollectionJpaRepositoryCustom {

    private final Clock clock;
    private final EntityManager em;

    public BrawlPassSeasonCollectionJpaRepositoryCustomImpl(Clock clock, EntityManager em) {
        this.clock = clock;
        this.em = em;
    }

    @Override
    @Transactional
    public BrawlPassSeasonCollectionEntity getCurrentSeason() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        BrawlPassSeasonCollectionEntity seasonEntity = queryFactory.selectFrom(brawlPassSeasonCollectionEntity)
                .orderBy(brawlPassSeasonCollectionEntity.number.desc())
                .limit(1)
                .fetchOne();

        if (seasonEntity == null) {
            throw new IllegalStateException("There is no current season.");
        }

        OffsetDateTime now = OffsetDateTime.now(clock);
        if (seasonEntity.getEndTime().isBefore(now)) {
            BrawlPassSeasonCollectionEntity nextSeasonEntity = seasonEntity.next();
            em.persist(nextSeasonEntity);
            return nextSeasonEntity;
        }

        return seasonEntity;
    }
}
