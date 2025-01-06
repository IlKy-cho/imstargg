package com.imstargg.client.brawlstars.news;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BrawlStarsNewsClient {

    private static final Pattern ARCHIVE_HTML_DATA_PATTERN = Pattern.compile(
            "<script\\s+id=\"__NEXT_DATA__\"\\s+type=\"application/json\">(.*?)</script>", Pattern.DOTALL);

    private final URI baseUrl;
    private final ObjectMapper objectMapper;
    private final BrawlStarsNewsHtmlApi brawlStarsNewsHtmlApi;

    BrawlStarsNewsClient(
            String baseUrl,
            ObjectMapper objectMapper,
            BrawlStarsNewsHtmlApi brawlStarsNewsHtmlApi
    ) {
        this.baseUrl = URI.create(baseUrl);
        this.objectMapper = objectMapper;
        this.brawlStarsNewsHtmlApi = brawlStarsNewsHtmlApi;
    }

    public BrawlStarsNewsArchiveResponse getNewsArchive(String lang, int page) {
        try {
            String html = Objects.equals(lang, "en") 
                ? brawlStarsNewsHtmlApi.getNewsArchiveHtml(page)
                : brawlStarsNewsHtmlApi.getNewsArchiveHtml(lang, page);

            Matcher matcher = ARCHIVE_HTML_DATA_PATTERN.matcher(html);
            if (!matcher.find()) {
                throw new IllegalStateException("브롤스타즈 뉴스 아카이브 데이터를 찾을 수 없습니다. html: \n" + html);
            }

            ArchivePageDataDto pageData = objectMapper.readValue(
                    matcher.group(1), ArchivePageDataDto.class);
            return pageData.props().pageProps().toResponse().toArticleFullLinkUrl(baseUrl);
        } catch (Exception e) {
            throw BrawlStarsNewsClientException.of(e);
        }
    }
}
