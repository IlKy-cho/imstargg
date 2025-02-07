package com.imstargg.batch.job.club;

import com.imstargg.batch.domain.NewClub;
import com.imstargg.storage.db.core.club.ClubCollectionEntity;
import com.imstargg.storage.db.core.club.ClubMemberCollectionEntity;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.InitializingBean;

public class NewClubJobItemWriter implements ItemWriter<NewClub>, InitializingBean {

    private final JpaItemWriter<ClubCollectionEntity> clubJpaItemWriter;
    private final JpaItemWriter<ClubMemberCollectionEntity> clubMemberJpaItemWriter;

    public NewClubJobItemWriter(
            JpaItemWriter<ClubCollectionEntity> clubJpaItemWriter,
            JpaItemWriter<ClubMemberCollectionEntity> clubMemberJpaItemWriter
    ) {
        this.clubJpaItemWriter = clubJpaItemWriter;
        this.clubMemberJpaItemWriter = clubMemberJpaItemWriter;
    }

    public NewClubJobItemWriter(EntityManagerFactory emf) {
        this(
                new JpaItemWriterBuilder<ClubCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build(),
                new JpaItemWriterBuilder<ClubMemberCollectionEntity>()
                        .entityManagerFactory(emf)
                        .usePersist(true)
                        .build()
        );
    }

    @Override
    public void write(Chunk<? extends NewClub> chunk) throws Exception {
        clubJpaItemWriter.write(new Chunk<>(
                chunk.getItems().stream().map(NewClub::club).toList())
        );
        clubMemberJpaItemWriter.write(new Chunk<>(
                chunk.getItems().stream().flatMap(newClub -> newClub.clubMembers().stream()).toList())
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        clubJpaItemWriter.afterPropertiesSet();
        clubMemberJpaItemWriter.afterPropertiesSet();
    }
}
