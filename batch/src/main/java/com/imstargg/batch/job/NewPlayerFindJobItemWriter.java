package com.imstargg.batch.job;

import com.imstargg.batch.domain.PlayerTagSet;
import com.imstargg.storage.db.core.UnknownPlayerCollectionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NewPlayerFindJobItemWriter implements ItemWriter<UnknownPlayerCollectionEntity>, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(NewPlayerFindJobItemWriter.class);

    private final JpaItemWriter<UnknownPlayerCollectionEntity> jpaItemWriter;
    private final PlayerTagSet playerTagSet;

    public NewPlayerFindJobItemWriter(
            JpaItemWriter<UnknownPlayerCollectionEntity> jpaItemWriter,
            PlayerTagSet playerTagSet
    ) {
        this.jpaItemWriter = jpaItemWriter;
        this.playerTagSet = playerTagSet;
    }

    public NewPlayerFindJobItemWriter(EntityManagerFactory emf, PlayerTagSet playerTagSet) {
        this(
                new JpaItemWriterBuilder<UnknownPlayerCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build(),
                playerTagSet
        );
    }

    @Override
    public void write(Chunk<? extends UnknownPlayerCollectionEntity> chunk) throws Exception {
        Map<String, ? extends UnknownPlayerCollectionEntity> tagToEntity = chunk.getItems().stream()
            .collect(Collectors.toMap(
                UnknownPlayerCollectionEntity::getBrawlStarsTag,
                Function.identity(),
                (existing, replacement) -> existing
            ));

        List<String> filteredTags = playerTagSet.filter(tagToEntity.keySet());

        jpaItemWriter.write(new Chunk<>(
                filteredTags.stream()
                        .map(tagToEntity::get)
                        .toList()
        ));

        log.debug("총 {}명의 신규 플레이어를 찾았습니다.", filteredTags.size());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jpaItemWriter.afterPropertiesSet();
    }
}
