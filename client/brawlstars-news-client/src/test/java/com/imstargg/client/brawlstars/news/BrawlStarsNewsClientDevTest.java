package com.imstargg.client.brawlstars.news;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("develop")
class BrawlStarsNewsClientDevTest {

    @Autowired
    private BrawlStarsNewsClient brawlStarsNewsClient;

    @Test
    void 한글_뉴스를_가져온다() {
        BrawlStarsNewsArchiveResponse newsArchive = brawlStarsNewsClient.getNewsArchive("ko", 1);
        System.out.println(newsArchive);
    }

    @Test
    void 영어_뉴스를_가져온다() {
        BrawlStarsNewsArchiveResponse newsArchive = brawlStarsNewsClient.getNewsArchive("en", 1);
        System.out.println(newsArchive);
    }
}