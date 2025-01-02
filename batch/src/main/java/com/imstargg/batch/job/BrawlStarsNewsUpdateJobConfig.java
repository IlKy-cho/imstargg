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
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.imstargg.storage.db.core.brawlstars.QBrawlStarsNewsCollectionEntity.brawlStarsNewsCollectionEntity;
import static java.util.stream.Collectors.toMap;

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
                .incrementer(new RunIdIncrementer())
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
                    Assert.notNull(em, "EntityManager가 null입니다.");

                    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
                    List<BrawlStarsNewsCollectionEntity> results = new ArrayList<>();

                    for (Language lang : Language.values()) {

                        int page = 1;
                        while (true) {
                            BrawlStarsNewsArchiveResponse newsArchiveResponse = brawlStarsNewsClient
                                    .getNewsArchive(lang.getCode(), page);

                            List<BrawlStarsNewsArticleResponse> articleResponseList = newsArchiveResponse.articles();
                            Map<String, BrawlStarsNewsCollectionEntity> existingNews = fetchExistingNews(
                                    queryFactory, articleResponseList);

                            List<BrawlStarsNewsCollectionEntity> newsEntities = updateNewNews(
                                    lang, articleResponseList, existingNews);

                            results.addAll(newsEntities);

                            debugLog(lang, page, articleResponseList, existingNews, newsEntities);

                            if (page == newsArchiveResponse.pageNumbers().getLast()
                                    || newsEntities.size() < articleResponseList.size()) {
                                break;
                            }
                            page += 1;
                        }
                    }

                    if (!results.isEmpty()) {
                        log.info("새로운 뉴스 {}개를 저장합니다.", results.size());
                        results.forEach(em::persist);
                    }

                    return RepeatStatus.FINISHED;
                }, txManager).build();
    }

    private Map<String, BrawlStarsNewsCollectionEntity> fetchExistingNews(
            JPAQueryFactory queryFactory, List<BrawlStarsNewsArticleResponse> articleResponseList) {
        return queryFactory.selectFrom(brawlStarsNewsCollectionEntity)
                .where(brawlStarsNewsCollectionEntity.linkUrl.in(
                        articleResponseList.stream()
                                .map(BrawlStarsNewsArticleResponse::linkUrl)
                                .toList()
                ))
                .fetch()
                .stream()
                .collect(toMap(BrawlStarsNewsCollectionEntity::getLinkUrl, Function.identity()));
    }

    private List<BrawlStarsNewsCollectionEntity> updateNewNews(
            Language lang,
            List<BrawlStarsNewsArticleResponse> articleResponseList,
            Map<String, BrawlStarsNewsCollectionEntity> existingNews
    ) {
        return articleResponseList.stream()
                .filter(article -> !existingNews.containsKey(article.linkUrl()))
                .map(article -> new BrawlStarsNewsCollectionEntity(
                        lang,
                        article.title(),
                        article.linkUrl(),
                        article.publishDate()
                )).toList();
    }

    private void debugLog(
            Language lang,
            int page,
            List<BrawlStarsNewsArticleResponse> articleResponseList,
            Map<String, BrawlStarsNewsCollectionEntity> existingNews,
            List<BrawlStarsNewsCollectionEntity> newsEntities
    ) {
        if (log.isDebugEnabled()) {
            log.debug("뉴스 아카이브[page({}), lang({})]를 조회했습니다. "
                            + "총 {}개의 뉴스 중 {}개가 이미 존재합니다. "
                            + "새로운 뉴스 {}개를 저장합니다.",
                    page, lang, articleResponseList.size(), existingNews.size(), newsEntities.size());
            log.debug("아카이브 내 뉴스: {}", articleResponseList.stream()
                    .map(BrawlStarsNewsArticleResponse::linkUrl).toList());
            log.debug("존재하는 뉴스: {}", existingNews.keySet());
            log.debug("새로운 뉴스: {}", newsEntities.stream()
                    .map(BrawlStarsNewsCollectionEntity::getLinkUrl).toList());
        }
    }

}
