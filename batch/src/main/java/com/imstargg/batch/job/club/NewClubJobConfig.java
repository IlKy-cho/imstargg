package com.imstargg.batch.job.club;

import com.imstargg.batch.domain.NewClub;
import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.batch.util.JPAQueryFactoryUtils;
import com.imstargg.client.brawlstars.BrawlStarsClient;
import com.imstargg.storage.db.core.club.ClubCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.imstargg.storage.db.core.QPlayerCollectionEntity.playerCollectionEntity;
import static com.imstargg.storage.db.core.club.QClubCollectionEntity.clubCollectionEntity;

@Configuration
public class NewClubJobConfig {

    private static final Logger log = LoggerFactory.getLogger(NewClubJobConfig.class);

    private static final String JOB_NAME = "newClubJob";
    private static final String STEP_NAME = "newClubStep";
    private static final int CHUNK_SIZE = 10;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;

    private final AlertManager alertManager;
    private final BrawlStarsClient brawlStarsClient;

    NewClubJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            BrawlStarsClient brawlStarsClient
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.brawlStarsClient = brawlStarsClient;
    }

    @Bean(JOB_NAME)
    Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .validator(new DefaultJobParametersValidator(new String[]{}, new String[]{"id.from", "id.to"}))
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    Step step() {
        StepBuilder stepBuilder = new StepBuilder(STEP_NAME, jobRepository);
        return stepBuilder
                .<String, NewClub>chunk(CHUNK_SIZE, txManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())

                .faultTolerant()
                .backOffPolicy(new FixedBackOffPolicy())
                .skipLimit(5)
                .skip(SQLIntegrityConstraintViolationException.class)
                .listener(new ItemWriteListener<>() {
                    @Override
                    public void onWriteError(Exception exception, Chunk<? extends NewClub> items) {
                        log.warn("{} 중 클럽 추가 충돌 발생. tags={}",
                                JOB_NAME,
                                items.getItems().stream()
                                        .map(NewClub::club)
                                        .map(ClubCollectionEntity::getBrawlStarsTag)
                                        .toList(),
                                exception
                        );
                    }
                })
                .build();
    }

    @Bean(STEP_NAME + "ItemReader")
    @StepScope
    ListItemReader<String> reader() {
        JPAQueryFactory queryFactory = JPAQueryFactoryUtils.getQueryFactory(emf);
        Set<String> clubTags = new HashSet<>(
                queryFactory
                        .selectDistinct(playerCollectionEntity.brawlStarsClubTag)
                        .from(playerCollectionEntity)
                        .where(playerCollectionEntity.brawlStarsClubTag.isNotNull())
                        .fetch()
        );

        boolean hasMore = true;
        int size = 100_000;
        String cursorTag = null;
        while (hasMore) {
            List<String> existsClubTags = queryFactory
                    .select(clubCollectionEntity.brawlStarsTag)
                    .from(clubCollectionEntity)
                    .where(
                            cursorTag != null ? clubCollectionEntity.brawlStarsTag.gt(cursorTag) : null
                    )
                    .orderBy(clubCollectionEntity.brawlStarsTag.asc())
                    .fetch();

            if (existsClubTags.isEmpty()) {
                break;
            }
            if (existsClubTags.size() < size) {
                hasMore = false;
            }

            cursorTag = existsClubTags.getLast();

            existsClubTags.forEach(clubTags::remove);
        }

        return new ListItemReader<>(clubTags.stream().toList());
    }

    @Bean(STEP_NAME + "ItemProcessor")
    @StepScope
    NewClubJobItemProcessor processor() {
        return new NewClubJobItemProcessor(brawlStarsClient);
    }

    @Bean(STEP_NAME + "ItemWriter")
    @StepScope
    NewClubJobItemWriter writer() {
        return new NewClubJobItemWriter(emf);
    }
}
