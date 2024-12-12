package com.imstargg.batch.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;
import static com.imstargg.storage.db.core.QUnknownPlayerCollectionEntity.unknownPlayerCollectionEntity;

public class PlayerTagSet {

    private final ConcurrentSkipListSet<String> tagCache = new ConcurrentSkipListSet<>();

    private final JPAQueryFactory queryFactory;

    public PlayerTagSet(EntityManagerFactory emf) {
        this.queryFactory = new JPAQueryFactory(emf.createEntityManager());
    }

    public boolean contains(String tag) {
        return tagCache.contains(tag);
    }

    public List<String> filter(Collection<String> tags) {
        Set<String> tagSet = tags.stream()
                .filter(tag -> !tagCache.contains(tag))
                .collect(Collectors.toSet());
        tagCache.addAll(tagSet);

        queryFactory.select(playerCollectionEntity.brawlStarsTag)
                .from(playerCollectionEntity)
                .where(playerCollectionEntity.brawlStarsTag.in(tagSet))
                .fetch()
                .forEach(tagSet::remove);

        queryFactory.select(unknownPlayerCollectionEntity.brawlStarsTag)
                .from(unknownPlayerCollectionEntity)
                .where(unknownPlayerCollectionEntity.brawlStarsTag.in(tagSet))
                .fetch()
                .forEach(tagSet::remove);

        return tagSet.stream().toList();
    }
}
