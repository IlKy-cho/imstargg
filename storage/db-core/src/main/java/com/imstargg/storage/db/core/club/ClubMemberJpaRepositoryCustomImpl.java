package com.imstargg.storage.db.core.club;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.imstargg.storage.db.core.club.QClubEntity.clubEntity;
import static com.imstargg.storage.db.core.club.QClubMemberEntity.clubMemberEntity;

class ClubMemberJpaRepositoryCustomImpl implements ClubMemberJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ClubMemberJpaRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ClubMemberEntity> findAllByClubBrawlStarsTagAndDeletedFalse(String clubBrawlStarsTag) {
        return queryFactory.selectFrom(clubMemberEntity)
                .join(clubEntity).on(clubMemberEntity.clubId.eq(clubEntity.id))
                .where(
                        clubEntity.brawlStarsTag.eq(clubBrawlStarsTag),
                        clubMemberEntity.deleted.isFalse()
                ).fetch();

    }
}
