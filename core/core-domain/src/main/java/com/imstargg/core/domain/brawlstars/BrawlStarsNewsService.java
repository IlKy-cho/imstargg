package com.imstargg.core.domain.brawlstars;

import com.imstargg.client.brawlstars.news.BrawlStarsNewsArchiveResponse;
import com.imstargg.client.brawlstars.news.BrawlStarsNewsClient;
import com.imstargg.client.brawlstars.news.BrawlStarsNewsClientException;
import com.imstargg.core.enums.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrawlStarsNewsService {

    private static final Logger log = LoggerFactory.getLogger(BrawlStarsNewsService.class);

    private final BrawlStarsNewsClient brawlStarsNewsClient;

    public BrawlStarsNewsService(BrawlStarsNewsClient brawlStarsNewsClient) {
        this.brawlStarsNewsClient = brawlStarsNewsClient;
    }

    public List<BrawlStarsNews> getNews(Language language, int page) {
        try {
            BrawlStarsNewsArchiveResponse archiveResponse = brawlStarsNewsClient.getNewsArchive(language.getCode(), page);
            return archiveResponse.articles().stream()
                    .map(article -> new BrawlStarsNews(
                            article.title(),
                            article.linkUrl(),
                            article.publishDate()
                    )).toList();
        } catch (BrawlStarsNewsClientException e) {
            log.info("브롤스타즈 뉴스를 가져오는 중 오류가 발생했습니다.", e);
            return List.of();
        }
    }
}
