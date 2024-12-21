package com.imstargg.client.brawlstars.news;

import com.imstargg.client.core.DefaultClientRetryConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        value = "brawlstars-news",
        url = "${app.client.brawlstars-news.url}",
        configuration = DefaultClientRetryConfig.class
)
interface BrawlStarsNewsHtmlApi {

    @GetMapping(value = "/en/games/brawlstars/blog/page/{page}/", consumes = MediaType.TEXT_HTML_VALUE)
    String getNewsArchiveHtml(@PathVariable int page);

    @GetMapping(value = "/en/games/brawlstars/{lang}/blog/page/{page}/", consumes = MediaType.TEXT_HTML_VALUE)
    String getNewsArchiveHtml(@PathVariable String lang, @PathVariable int page);
}
