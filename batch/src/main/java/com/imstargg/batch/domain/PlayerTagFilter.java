package com.imstargg.batch.domain;

import com.imstargg.batch.util.JPAQueryFactoryUtils;
import jakarta.persistence.EntityManagerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;

public class PlayerTagFilter {

    private final EntityManagerFactory emf;
    private final ConcurrentSkipListSet<String> cache;

    public PlayerTagFilter(EntityManagerFactory emf) {
        this.emf = emf;
        this.cache = new ConcurrentSkipListSet<>();
    }

    public List<String> filter(Collection<String> tags) {
        Set<String> tagSet = tags.stream()
                .filter(cache::add)
                .collect(Collectors.toSet());

        Set<String> existsTagSet = new HashSet<>(JPAQueryFactoryUtils.getQueryFactory(emf)
                .select(playerCollectionEntity.brawlStarsTag)
                .from(playerCollectionEntity)
                .where(playerCollectionEntity.brawlStarsTag.in(tagSet))
                .fetch());

        return tagSet.stream()
                .filter(tag -> !existsTagSet.contains(tag))
                .toList();
    }
}
