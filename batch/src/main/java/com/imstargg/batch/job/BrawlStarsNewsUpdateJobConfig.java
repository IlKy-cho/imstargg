package com.imstargg.batch.job;

import com.imstargg.batch.job.support.ExceptionAlertJobExecutionListener;
import com.imstargg.client.brawlstars.news.BrawlStarsNewsArchiveResponse;
import com.imstargg.client.brawlstars.news.BrawlStarsNewsArticleResponse;
import com.imstargg.client.brawlstars.news.BrawlStarsNewsClient;
import com.imstargg.core.enums.Language;
import com.imstargg.storage.db.core.brawlstars.BrawlStarsNewsCollectionEntity;
import com.imstargg.support.alert.AlertManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.imstargg.storage.db.core.brawlstars.QBrawlStarsNewsCollectionEntity.brawlStarsNewsCollectionEntity;

@Configuration
class BrawlStarsNewsUpdateJobConfig {

    private static final Logger log = LoggerFactory.getLogger(BrawlStarsNewsUpdateJobConfig.class);

    private static final String JOB_NAME = "brawlStarsNewsUpdateJob";
    private static final String STEP_NAME = "brawlStarsNewsUpdateStep";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final EntityManagerFactory emf;
    private final AlertManager alertManager;
    private final BrawlStarsNewsClient brawlStarsNewsClient;

    public BrawlStarsNewsUpdateJobConfig(
            JobRepository jobRepository,
            PlatformTransactionManager txManager,
            EntityManagerFactory emf,
            AlertManager alertManager,
            BrawlStarsNewsClient brawlStarsNewsClient
    ) {
        this.jobRepository = jobRepository;
        this.txManager = txManager;
        this.emf = emf;
        this.alertManager = alertManager;
        this.brawlStarsNewsClient = brawlStarsNewsClient;
    }

    @Bean(JOB_NAME)
    public Job job() {
        JobBuilder jobBuilder = new JobBuilder(JOB_NAME, jobRepository);
        return jobBuilder
                .start(step())
                .listener(new ExceptionAlertJobExecutionListener(alertManager))
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    public Step step() {
        TaskletStepBuilder taskletStepBuilder = new TaskletStepBuilder(new StepBuilder(STEP_NAME, jobRepository));
        return taskletStepBuilder
                .tasklet((contribution, chunkContext) -> {
                    EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(emf);
                    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
                    List<BrawlStarsNewsCollectionEntity> newNewsEntities = new ArrayList<>();

                    for (Language lang : Language.values()) {

                        int page = 1;
                        while (true) {
                            BrawlStarsNewsArchiveResponse newsArchiveResponse = brawlStarsNewsClient
                                    .getNewsArchive(lang.getCode(), page);

                            List<BrawlStarsNewsArticleResponse> articleResponseList = newsArchiveResponse.articles();
                            Set<String> existingUrl = fetchExistingUrls(queryFactory, articleResponseList);

                            List<BrawlStarsNewsCollectionEntity> newsEntities = fetchNotExistsNewsList(
                                    lang, articleResponseList, existingUrl);
                            newNewsEntities.addAll(newsEntities);

                            if (log.isDebugEnabled()) {
                                log.debug("뉴스 아카이브[page({}), lang({})]를 조회했습니다. "
                                        + "총 {}개의 뉴스 중 {}개가 이미 존재합니다. "
                                        + "새로운 뉴스 {}개를 저장합니다.",
                                        page, lang, articleResponseList.size(), existingUrl.size(), newsEntities.size());
                                log.debug("아카이브 내 뉴스: {}", articleResponseList.stream()
                                        .map(BrawlStarsNewsArticleResponse::linkUrl).toList());
                                log.debug("존재하는 뉴스: {}", existingUrl);
                                log.debug("새로운 뉴스: {}", newsEntities.stream()
                                        .map(BrawlStarsNewsCollectionEntity::getLinkUrl).toList());
                            }

                            if (page == newsArchiveResponse.pageNumbers().getLast()
                                    || newsEntities.size() < articleResponseList.size()) {
                                break;
                            }
                            page += 1;
                        }
                    }

                    if (!newNewsEntities.isEmpty()) {
                        log.info("새로운 뉴스 {}개를 저장합니다.", newNewsEntities.size());
                        newNewsEntities.forEach(em::persist);
                    }

                    return RepeatStatus.FINISHED;
                }, txManager).build();
    }

    private static Set<String> fetchExistingUrls(
            JPAQueryFactory queryFactory, List<BrawlStarsNewsArticleResponse> articleResponseList) {
        return Set.copyOf(queryFactory.select(brawlStarsNewsCollectionEntity.linkUrl)
                .from(brawlStarsNewsCollectionEntity)
                .where(brawlStarsNewsCollectionEntity.linkUrl.in(
                        articleResponseList.stream()
                                .map(BrawlStarsNewsArticleResponse::linkUrl)
                                .toList()
                ))
                .fetch());
    }

    private static List<BrawlStarsNewsCollectionEntity> fetchNotExistsNewsList(
            Language lang, List<BrawlStarsNewsArticleResponse> articleResponseList, Set<String> existingUrl) {
        return articleResponseList.stream()
                .filter(article -> !existingUrl.contains(article.linkUrl()))
                .map(article -> new BrawlStarsNewsCollectionEntity(
                        lang,
                        article.title(),
                        article.linkUrl(),
                        article.publishDate()
                )).toList();
    }

}
