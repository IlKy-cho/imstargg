package com.imstargg.batch.job;

import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;
import static com.imstargg.storage.db.core.QUnknownPlayerCollectionEntity.unknownPlayerCollectionEntity;

public class NewPlayerFindJobItemWriter implements ItemWriter<List<UnknownPlayerCollectionEntity>>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerFindJobItemWriter.class);

    private final JpaItemWriter<UnknownPlayerCollectionEntity> jpaItemWriter;
    private final EntityManagerFactory emf;

    public NewPlayerFindJobItemWriter(
            JpaItemWriter<UnknownPlayerCollectionEntity> jpaItemWriter,
            EntityManagerFactory emf
    ) {
        this.jpaItemWriter = jpaItemWriter;
        this.emf = emf;
    }

    public NewPlayerFindJobItemWriter(EntityManagerFactory emf) {
        this(
                new JpaItemWriterBuilder<UnknownPlayerCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build(),
                emf
        );
    }

    @Override
    public void write(Chunk<? extends List<UnknownPlayerCollectionEntity>> chunk) throws Exception {
        Map<String, ? extends UnknownPlayerCollectionEntity> tagToEntity = chunk.getItems().stream()
                .flatMap(List::stream)
                .collect(Collectors.toMap(
                        UnknownPlayerCollectionEntity::getBrawlStarsTag,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));

        JPAQueryFactory queryFactory = getQueryFactory();

        Set<String> existingTags = Stream.of(
                        queryFactory.select(playerCollectionEntity.brawlStarsTag)
                                .from(playerCollectionEntity)
                                .where(playerCollectionEntity.brawlStarsTag.in(tagToEntity.keySet()))
                                .fetch().stream(),
                        queryFactory.select(unknownPlayerCollectionEntity.brawlStarsTag)
                                .from(unknownPlayerCollectionEntity)
                                .where(unknownPlayerCollectionEntity.brawlStarsTag.in(tagToEntity.keySet()))
                                .fetch().stream()
                ).flatMap(Function.identity())
                .collect(Collectors.toSet());

        List<? extends UnknownPlayerCollectionEntity> newPlayers = tagToEntity.entrySet().stream()
                .filter(entry -> !existingTags.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .toList();
        jpaItemWriter.write(new Chunk<>(
                newPlayers
        ));

        log.debug("총 {}명의 신규 플레이어를 찾았습니다.", newPlayers.size());
    }

    private JPAQueryFactory getQueryFactory() {
        EntityManager entityManager = EntityManagerFactoryUtils.getTransactionalEntityManager(emf);
        return new JPAQueryFactory(entityManager);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jpaItemWriter.afterPropertiesSet();
    }
}
