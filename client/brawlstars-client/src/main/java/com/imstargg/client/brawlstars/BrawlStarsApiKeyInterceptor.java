package com.imstargg.client.brawlstars;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class BrawlStarsApiKeyInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(BrawlStarsApiKeyInterceptor.class);

    private final List<String> keys;
    private final AtomicInteger keyIndex = new AtomicInteger();

    public BrawlStarsApiKeyInterceptor(List<String> keys) {
        this.keys = keys;
        log.debug("Brawl Stars API keys count: {}", keys.size());
        if (keys.isEmpty()) {
            throw new IllegalArgumentException("Brawl Stars API key is required");
        }
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        int index = keyIndex.getAndIncrement();
        String key = keys.get(index % keys.size());
        requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + key);
    }
}
